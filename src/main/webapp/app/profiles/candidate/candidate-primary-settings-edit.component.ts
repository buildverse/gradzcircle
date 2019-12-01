import { Component, OnInit, OnDestroy } from '@angular/core';
import { Candidate } from '../../entities/candidate/candidate.model';
import { CandidateService } from '../../entities/candidate/candidate.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import 'rxjs/add/operator/debounceTime';
import { Observable } from 'rxjs/Observable';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { JobCategory } from '../../entities/job-category/job-category.model';
import { Gender } from '../../entities/gender/gender.model';
import { IMultiSelectOption, IMultiSelectSettings, IMultiSelectTexts } from 'angular-2-dropdown-multiselect';
import { JhiEventManager } from 'ng-jhipster';
import { JobCategoryService } from '../../entities//job-category/job-category.service';
import { NationalityService } from '../../entities/nationality/nationality.service';
import { CountryService } from '../../entities/country/country.service';
import { CandidateProfileSettingService } from './candidate-profile-setting.service';
import { Subscription } from 'rxjs';

/**
 * @TODO:
 *
 * 1. Update validations for type ahead need to ensure valid value is selected
 * 2. Clean up console logs
 * 3. Add config for url PROD vs DEV
 * 4. ADDRESS HANDLING IS VERY HACKY NEED TO FIX THIS AS PART OF UPLIFT.
 *
 */

// const URL = 'http://localhost:8080/api/upload'

@Component({
    moduleId: module.id,
    selector: 'jhi-candidate-primary-settings-edit',
    templateUrl: 'candidate-primary-settings-edit.component.html'
})
export class CandidatePrimarySettingsEditComponent implements OnInit, OnDestroy {
    primarySettingForm: FormGroup;
    candidate: Candidate;
    errorMessage: String;
    jobCategories: JobCategory[];
    candidatCoreFields: boolean;
    genders: Gender[];
    careerInterestOptions: IMultiSelectOption[];
    charsLeft: number;
    maxAboutMeLength: number;
    subscription: Subscription;
    profileSubscriber: Subscription;
    mySettings: IMultiSelectSettings = {
        enableSearch: true,
        checkedStyle: 'fontawesome',
        itemClasses: 'text-muted',
        dynamicTitleMaxItems: 3,
        displayAllSelectedText: true,
        selectionLimit: 5,
        containerClasses: 'dropdown-inline text-muted',
        buttonClasses: 'btn btn-outline-primary'
    };

    // Text configuration
    myTexts: IMultiSelectTexts = {
        checked: 'item selected',
        checkedPlural: 'items selected',
        searchPlaceholder: 'Search..',
        defaultTitle: 'Select career Interests',
        allSelected: 'All selected'
    };

    constructor(
        private formBuilder: FormBuilder,
        private candidateService: CandidateService,
        private route: ActivatedRoute,
        private router: Router,
        private config: NgbDatepickerConfig,
        private eventManager: JhiEventManager,
        private jobCategoryService: JobCategoryService,
        private nationalityService: NationalityService,
        private countryService: CountryService,
        private candidateSettingService: CandidateProfileSettingService
    ) {}

    ngOnInit() {
        this.profileSubscriber = this.candidateSettingService
            .getCandidateFromParentToChild()
            .subscribe(candidate => (this.candidate = candidate));
        this.route.data.subscribe((data: { jobCategory: any }) => (this.jobCategories = data.jobCategory));
        this.route.data.subscribe((data: { gender: any }) => (this.genders = data.gender));
        // console.log('Candidates ' + JSON.stringify(this.candidate));
        this.careerInterestOptions = new Array();
        this.jobCategories.forEach(item => {
            this.careerInterestOptions.push({ id: item.id, name: item.jobCategory });
        });
        this.maxAboutMeLength = 150;
        this.primarySettingForm = this.formBuilder.group({
            firstName: [null, [Validators.required]],
            lastName: [null, [Validators.required]],
            aboutMe: [null],
            email: [null, [Validators.required, Validators.pattern('[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+')]],
            jobCategories: [null, [Validators.required]],
            differentlyAbled: [null],
            gender: [null, [Validators.required]]
        });
        this.onCandidateRetrieved();
        this.primarySettingForm
            .get('jobCategories')
            .valueChanges.subscribe(value => this.reCreateJobCategoryModelFromSelection(String(value)));
        this.characterCount();
    }

    requestJobCategoryData = (text: string): Observable<Response> => {
        return this.jobCategoryService
            .searchRemote({
                query: text
            })
            .map(data => data.body);
    };

    requestNationalityData = (text: string): Observable<Response> => {
        return this.nationalityService
            .searchRemote({
                query: text
            })
            .map(data => data.body);
    };

    requestCountryData = (text: string): Observable<Response> => {
        return this.countryService
            .searchRemote({
                query: text
            })
            .map(data => data.body);
    };

    reCreateJobCategoryModelFromSelection(value) {
        let jobCategoryReformatted;
        jobCategoryReformatted = new Array();
        this.jobCategories.forEach(element => {
            for (let j = 0; j < value.length; j++) {
                if (value[j] === element.id) {
                    jobCategoryReformatted.push(element);
                }
            }
        });
        return jobCategoryReformatted;
    }

    configureDatePicker() {
        this.config.minDate = { year: 1980, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };

        // days that don't belong to current month are not visible
        this.config.outsideDays = 'hidden';
    }

    onCandidateRetrieved(): void {
        if (this.primarySettingForm) {
            this.primarySettingForm.reset();
        }
        const categories = new Array();
        for (let j = 0; j < this.candidate.jobCategories.length; j++) {
            categories.push(this.candidate.jobCategories[j].id);
        }
        // Update the data on the form
        this.primarySettingForm.patchValue({
            firstName: this.candidate.firstName,
            lastName: this.candidate.lastName,
            aboutMe: this.candidate.aboutMe,
            email: this.candidate.login.email,
            jobCategories: categories,
            differentlyAbled: this.candidate.differentlyAbled,
            gender: this.candidate.gender
        });
    }

    compareSelectControlValues(entity1: any, entity2: any): boolean {
        return entity1 && entity2 ? entity1.id === entity2.id : entity1 === entity2;
    }

    save(): void {
        if (this.primarySettingForm.dirty && this.primarySettingForm.valid) {
            const candidate = this.prepareCandidateBeforeSave();
            this.candidateService.update(candidate).subscribe(
                () => this.onSaveComplete(),
                (error: any) => {
                    this.errorMessage = <any>error;
                    this.primarySettingForm.reset();
                    this.router.navigate(['/error']);
                    return Observable.of(null);
                }
            );
        } else {
            if (!this.primarySettingForm.dirty) {
                this.onSaveComplete();
            }
        }
    }

    prepareCandidateBeforeSave(): any {
        const candidate = Object.assign({}, this.candidate, this.primarySettingForm.value);
        candidate.jobCategories = this.reCreateJobCategoryModelFromSelection(candidate.jobCategories);
        //  this.setCandidateAddress(candidate);
        return candidate;
    }

    onSaveComplete(): void {
        this.primarySettingForm.reset();
        this.eventManager.broadcast({ name: 'candidatePrimarySettingModification', content: 'OK' });
        this.candidateSettingService.changeSetting('primarySetting');
        this.router.navigate(['/', 'candidate-profile']);
    }

    characterCount() {
        if (this.primarySettingForm.get('aboutMe').value) {
            this.charsLeft = this.maxAboutMeLength - this.primarySettingForm.get('aboutMe').value.length;
        } else {
            this.charsLeft = this.maxAboutMeLength;
        }
    }

    ngOnDestroy() {
        if (this.subscription) {
            this.eventManager.destroy(this.subscription);
        }
        if (this.profileSubscriber) {
            this.eventManager.destroy(this.profileSubscriber);
        }
    }
}
