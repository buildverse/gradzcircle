import {Component, OnInit, ViewChild} from '@angular/core';
import {UserService} from '../../shared/';
import {Principal} from '../../shared/auth/principal.service';
import {Candidate} from '../../entities/candidate/candidate.model';
import {CandidateService} from '../../entities/candidate/candidate.service';
import {FormGroup, FormBuilder, Validators } from '@angular/forms';
import { JhiDateUtils} from 'ng-jhipster';
import {Account} from '../../shared';
import {BaseEntity} from '../../shared/model/base-entity';
import 'rxjs/add/operator/debounceTime';
import {Observable} from 'rxjs/Observable';
import { NgbDatepickerConfig} from '@ng-bootstrap/ng-bootstrap';
import {ActivatedRoute, Router} from '@angular/router';
import {JobCategory} from '../../entities/job-category/job-category.model';
import {MaritalStatus} from '../../entities/marital-status/marital-status.model';
import {Country} from '../../entities/country/country.model';
import {Gender} from '../../entities/gender/gender.model';
import {Language} from '../../entities/language/language.model';
import {Skills} from '../../entities/skills/skills.model';
import {VisaType} from '../../entities/visa-type/visa-type.model';
import {JobType} from '../../entities/job-type/job-type.model';
import {EmploymentType} from '../../entities/employment-type/employment-type.model';
import {IMultiSelectOption, IMultiSelectSettings, IMultiSelectTexts} from 'angular-2-dropdown-multiselect';

import {FileUploader, FileLikeObject} from 'ng2-file-upload';
import {Address} from '../../entities/address/address.model';

import {Nationality} from '../../entities/nationality/nationality.model';
import {LocalStorageService, SessionStorageService} from 'ngx-webstorage';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

//import {ImageCropperComponent, CropperSettings} from 'ng2-img-cropper';
import {SERVER_API_URL} from '../../app.constants';
import {JobCategoryService} from '../../entities//job-category/job-category.service';
import {NationalityService} from '../../entities/nationality/nationality.service';
import {CountryService} from '../../entities/country/country.service';


/**
 * @TODO: 
 *
 * 1. Update validations for type ahead need to ensure valid value is selected
 * 2. Clean up console logs
 * 3. Add config for url PROD vs DEV
 * 4. ADDRESS HANDLING IS VERY HACKY NEED TO FIX THIS AS PART OF UPLIFT.
 * 
 */



//const URL = 'http://localhost:8080/api/upload'

@Component({
  moduleId: module.id,
  selector: 'edit-profile',
  templateUrl: 'candidate-about-me-edit.component.html',
  styleUrls: ['candidate.css']
})

export class CandidateProfileAboutMeEditComponent implements OnInit {

  @ViewChild('selectedPicture')
  selectedPicture: any;

  candidateAboutMeForm: FormGroup;
  candidate: Candidate;
  address: Address;
  account: Account;
  errorMessage: String;
  buttonText: String;
  jobCategories: JobCategory[];
  maritalStatuses: MaritalStatus[];
  candidatCoreFields: boolean;
  countries: Country[];
  genders: Gender[];
  languagesMetaData: Language[];
  skills: Skills[];
  visaTypes: VisaType[];
  jobType: JobType[];
  employmentType: EmploymentType[];
  nationalities: Nationality[];
  careerInterestOptions: IMultiSelectOption[];
  uploader: FileUploader;
  fileUploadErrorMessage: string;
  allowedMimeType = ['image/png', 'image/jpg', 'image/jpeg', 'image/gif'];
  maxFileSize = 100 * 1024;
  imageDataNotAvailable: boolean;
  enableLanguageProfiency: boolean;
  croppedImage: any;
  charsLeft: number;
  maxAboutMeLength: number;
  queueLimit = 1;

  mySettings: IMultiSelectSettings = {
    enableSearch: true,
    checkedStyle: 'fontawesome',
    itemClasses: 'text-muted',
    dynamicTitleMaxItems: 3,
    displayAllSelectedText: true,
    selectionLimit: 5,
    containerClasses: 'dropdown-inline text-muted'
  };

  // Text configuration
  myTexts: IMultiSelectTexts = {
    checked: 'item selected',
    checkedPlural: 'items selected',
    searchPlaceholder: 'Search..',
    defaultTitle: 'Select at least one career Interest',
    allSelected: 'All selected'
  };

  // get languages(): FormArray{
  //     return <FormArray>this.candidateAboutMeForm.get('candidateLanguageProficiency');
  // }

  constructor(
    private principal: Principal,
    private formBuilder: FormBuilder,
    private candidateService: CandidateService,
    private route: ActivatedRoute,
    private router: Router,
    private config: NgbDatepickerConfig,
    private dateUtils: JhiDateUtils,
    private localStorage: LocalStorageService,
    private sessionStorage: SessionStorageService,
    private eventManager: JhiEventManager,
    private userService: UserService,
    private jobCategoryService: JobCategoryService,
    private nationalityService: NationalityService,
    private countryService: CountryService
    //  private cropperSettings: CropperSettings

  ) {}


  ngOnInit() {

    this.imageDataNotAvailable = false;
    this.route.data.subscribe((data: {jobCategory: any}) => this.jobCategories = data.jobCategory);
    this.route.data.subscribe((data: {maritalStatus: any}) => this.maritalStatuses = data.maritalStatus);
    this.route.data.subscribe((data: {gender: any}) => this.genders = data.gender);
    this.route.data.subscribe((data: {candidate: any}) => this.candidate = data.candidate.body);

    if (!this.principal.getImageUrl()) {
      this.imageDataNotAvailable = true;
    }
    //   this.initializeCropper(); 
    this.careerInterestOptions = new Array();
    this.jobCategories.forEach((item) => {
      this.careerInterestOptions.push({id: item.id, name: item.jobCategory});
    });
    this.configureDatePicker();
    const token = this.localStorage.retrieve('authenticationToken') || this.sessionStorage.retrieve('authenticationToken');
    this.uploader = new FileUploader({
      url: SERVER_API_URL + 'api/upload/' + this.candidate.login.id,
      allowedMimeType: this.allowedMimeType,
      maxFileSize: this.maxFileSize,
      queueLimit: this.queueLimit,
      removeAfterUpload: true
    });
    this.uploader.authTokenHeader = 'Authorization';
    this.uploader.authToken = 'Bearer ' + token;
    this.maxAboutMeLength = 150;

    this.candidateAboutMeForm = this.formBuilder.group({
      firstName: [null, [Validators.required]],
      lastName: [null, [Validators.required]],
      middleName: [null],
      aboutMe: [null, [Validators.maxLength(150)]],
      email: [null, [Validators.required, Validators.pattern('[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+')]],
      //  dateOfBirth: [null, [Validators.required]],
      jobCategories: [null, [Validators.required]],
      maritalStatus: [null],
      nationality: [null, [Validators.required]],
      facebook: [null],
      twitter: [null],
      differentlyAbled: [null],
      gender: [null, [Validators.required]],
      visaType: [null],
      address: this.buildAddressGroup()

    });
    this.onCandidateRetrieved();

    this.uploader.onWhenAddingFileFailed = (item, filter, options) => this.onWhenAddingFileFailed(item, filter, options);
    this.uploader.onAfterAddingFile = (item) => this.onAfterAddingFile(item);
    // COMMENTING THIS FOR NOW.. EARLIER PLAN WAS TO HAVE ISD POPULATED ON COUNTRY SELECTION
    /* let control = <FormArray>this.candidateAboutMeForm.controls['address'];
     control.get('country').valueChanges.subscribe(value => this.onCountrySelect(value));*/
    this.candidateAboutMeForm.get('jobCategories').valueChanges
      .subscribe((value) => this.reCreateJobCategoryModelFromSelection(String(value)));
    this.characterCount();
  }

  // initializeCropper(){
  //     this.cropperSettings = new CropperSettings();
  //     this.cropperSettings.width = 100;
  //     this.cropperSettings.height = 100;
  //     this.cropperSettings.croppedWidth =256;
  //     this.cropperSettings.croppedHeight = 256;
  //     this.cropperSettings.canvasWidth = 300;
  //     this.cropperSettings.canvasHeight = 300;
  //     this.cropperSettings.cropperDrawSettings.strokeColor = 'rgba(255,255,255,1)';
  //     this.cropperSettings.cropperDrawSettings.strokeWidth = 2;
  //     this.croppedImage={};
  // }

  clearSelectedPicture() {
    this.uploader.clearQueue();
    this.selectedPicture.nativeElement.value = '';
  }

  requestJobCategoryData = (text: string): Observable<Response> => {
    return this.jobCategoryService.searchRemote({
      query: text
    }).map((data) => data.body);
  }

  requestNationalityData = (text: string): Observable<Response> => {
    return this.nationalityService.searchRemote({
      query: text
    }).map((data) => data.body);
  }

  requestCountryData = (text: string): Observable<Response> => {
    return this.countryService.searchRemote({
      query: text
    }).map((data) => data.body);
  }

  uploadImage(item) {
    item.upload();
    this.uploader.onCompleteItem = (item, response, status, header) => {
      if (status === 200) {
        this.eventManager.broadcast({name: 'candidateImageModification', content: 'OK'});
        this.router.navigate(['../../details'], {relativeTo: this.route});
      }
    };
  }
  onAfterAddingFile(item) {
    this.fileUploadErrorMessage = '';
  }
  
  onWhenAddingFileFailed(item: FileLikeObject, filter: any, options: any) {
    switch (filter.name) {
      case 'fileSize':
        this.fileUploadErrorMessage = `File size must not be more than 100 Kb`;
        break;
      case 'mimeType':
        const allowedTypes = this.allowedMimeType.join();
        this.fileUploadErrorMessage = `File types allowed : png,jpg,jpeg,gif`;
        break;
      case 'queueLimit':
        break;
      default:
        this.fileUploadErrorMessage = `Unknown error (filter is ${filter.name})`;
    }

  }

  reCreateJobCategoryModelFromSelection(value) {

    let jobCategoryReformatted;
    jobCategoryReformatted = new Array();
    this.jobCategories.forEach((element) => {
      for (let j = 0; j < value.length; j++) {
        if (value[j] === element.id) {
          jobCategoryReformatted.push(element);
        }
      }
    });
    return jobCategoryReformatted;
  }

  removeImage() {
    let status;
    this.userService.deleteImage(this.candidate.login.id).subscribe((response) => {
      status = response.status;
      if (status === 200) {
        this.eventManager.broadcast({name: 'candidateImageModification', content: 'OK'});
        this.router.navigate(['../../details'], {relativeTo: this.route});
      }
    });
  }

  buildAddressGroup(): FormGroup {
    return this.formBuilder.group({
      id: [null],
      addressLineOne: [null],
      addressLineTwo: [null],
      city: [null, [Validators.required]],
      state: [null, [Validators.required]],
      country: [null, [Validators.required]],
      zip: [null],
      phoneNumber: [null, [Validators.required, Validators.pattern('[0-9]+')]],
      phoneCode: ['+91', [Validators.required, Validators.pattern('[+.0-9]+')]]
    });

  }

  configureDatePicker() {
    this.config.minDate = {year: 1980, month: 1, day: 1};
    this.config.maxDate = {year: 2099, month: 12, day: 31};

    // days that don't belong to current month are not visible
    this.config.outsideDays = 'hidden';
  }

  /* searchNationality = (text$: Observable<string>) =>
    text$
      .debounceTime(100)
      .distinctUntilChanged()
      .map((term) => term.length < 2 ? []
        : this.nationalities.filter
          ((v) => v.nationality ? v.nationality.toLowerCase().indexOf(term.toLowerCase()) > -1 : '').slice(0, 10));

  nationalityFormatter = (n: {nationality: string}) => n.nationality;

  searchCountry = (text$: Observable<string>) =>
    text$
      .debounceTime(200)
      .distinctUntilChanged()
      .map((term) => term.length < 2 ? []
        : this.countries.filter(v => v.countryNiceName ?
          v.countryNiceName.toLowerCase().indexOf(term.toLowerCase()) > -1 : '').slice(0, 10));
  countryFormatter = (c: {countryNiceName: string}) => c.countryNiceName;
*/
  onCountrySelect(countrySelected) {
    if (!countrySelected) {
      this.candidateAboutMeForm.get('phoneCode').reset();
    }
    for (const country of this.countries) {
      if (countrySelected) {
        if (country.id === countrySelected.id) {
          this.candidateAboutMeForm.get('phoneCode').setValue('+' + country.phoneCode);
        }
      }
    }
  }

  onCandidateRetrieved(): void {
    if (this.candidateAboutMeForm) {
      this.candidateAboutMeForm.reset();
    }
    let jsonDate;
    const dateOfBirth = this.dateUtils.convertDateTimeFromServer(this.candidate.dateOfBirth);
    if (dateOfBirth) {
      jsonDate = {year: dateOfBirth.getFullYear(), month: dateOfBirth.getUTCMonth() + 1, day: dateOfBirth.getDate()};
    }
    const categories = new Array();
    for (let j = 0; j < this.candidate.jobCategories.length; j++) {
      categories.push(this.candidate.jobCategories[j].id);
    }
    //Update the data on the form
    this.candidateAboutMeForm.patchValue({
      firstName: this.candidate.login.firstName,
      lastName: this.candidate.login.lastName,
      middleName: this.candidate.middleName,
      aboutMe: this.candidate.aboutMe,
      email: this.candidate.login.email,
      dateOfBirth: jsonDate,
      jobCategories: categories,
      maritalStatus: this.candidate.maritalStatus,
      nationality: this.candidate.nationality,
      facebook: this.candidate.facebook,
      twitter: this.candidate.twitter,
      differentlyAbled: this.candidate.differentlyAbled,
      gender: this.candidate.gender,
      visaType: this.candidate.visaType,
      address: {
        id: this.candidate.addresses[0] ? this.candidate.addresses[0].id : '',
        addressLineOne: this.candidate.addresses[0] ? this.candidate.addresses[0].addressLineOne : '',
        addressLineTwo: this.candidate.addresses[0] ? this.candidate.addresses[0].addressLineTwo : '',
        city: this.candidate.addresses[0] ? this.candidate.addresses[0].city : '',
        state: this.candidate.addresses[0] ? this.candidate.addresses[0].state : '',
        country: this.candidate.addresses[0] ? this.candidate.addresses[0].country : '',
        zip: this.candidate.addresses[0] ? this.candidate.addresses[0].zip : '',
        phoneCode: this.candidate.phoneCode ? this.candidate.phoneCode : '+91',
        phoneNumber: this.candidate.phoneNumber,
      }
    });
  }

  compareSelectControlValues(entity1: BaseEntity, entity2: BaseEntity): boolean {
    return entity1 && entity2 ? entity1.id === entity2.id : entity1 === entity2;
  }

  /*setAddresses(addresses: Address[]) {
      const addressFormGroup = addresses.map(address => this.formBuilder.group(address));
      const adressFormArray = this.formBuilder.array(addressFormGroup);
      this.candidateAboutMeForm.setControl('addresses', adressFormArray);
  }
*/
  save(): void {
    if (this.candidateAboutMeForm.dirty && this.candidateAboutMeForm.valid) {
      const candidate = this.prepareCandidateBeforeSave();
      this.candidateService.update(candidate).subscribe(
        () => this.onSaveComplete(),
        (error: any) => {
          this.errorMessage = <any>error;
          this.candidateAboutMeForm.reset();
          this.router.navigate(['/error']);
          return Observable.of(null);
        }

      );

    } else {
      if (!this.candidateAboutMeForm.dirty) {
        this.onSaveComplete();
      }
    }
  }

  prepareCandidateBeforeSave(): any {
    const candidate = Object.assign({}, this.candidate, this.candidateAboutMeForm.value);
    candidate.jobCategories = this.reCreateJobCategoryModelFromSelection(candidate.jobCategories);
    this.setCandidateAddress(candidate);
    return candidate;
  }


  setCandidateAddress(candidate) {
    const addresses = new Array();
    const address = this.candidateAboutMeForm.get('address').value;
    // address.id = this.candidate.addresses[0] ? this.candidate.addresses[0].id : undefined;
    addresses.push(address);
    candidate.addresses = addresses;
    // Move candidate phone to cnaidate object
    candidate.phoneCode = address.phoneCode;
    candidate.phoneNumber = address.phoneNumber;
  }

  onSaveComplete(): void {
    this.candidateAboutMeForm.reset();
    this.eventManager.broadcast({name: 'candidateListModification', content: 'OK'});
    this.router.navigate(['../../details'], {relativeTo: this.route});
  }

 /* validNationality(c: AbstractControl): {[key: string]: boolean} | null {
    if (c.value === null)
      return null;
    let countrySelection = c.value;
    for (var counter = 0; counter < this.countries.length; counter++) {
      if (countrySelection === this.countries[counter]) {
        return null;
      }
      else
        return {'invalidCountry': true}
    }
    return null;
  }
*/
  characterCount() {
    if (this.candidateAboutMeForm.get('aboutMe').value) {
      this.charsLeft = this.maxAboutMeLength - this.candidateAboutMeForm.get('aboutMe').value.length;
    } else {
      this.charsLeft = this.maxAboutMeLength;
    }
  }


}