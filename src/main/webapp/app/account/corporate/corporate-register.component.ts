import {Component, OnInit, AfterViewInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators, AbstractControl} from '@angular/forms';
import {JhiLanguageService} from 'ng-jhipster';
import 'rxjs/add/operator/debounceTime';
import {Register} from '../register/register.service';
import {Country} from '../../entities/country/country.model';
import { EMAIL_ALREADY_USED_TYPE } from '../../shared';
import { LOGIN_ALREADY_USED_TYPE } from '../../shared/constants/error.constants';
import { HttpErrorResponse } from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';


function passwordMatcher(c: AbstractControl): {[key: string]: boolean} | null {
  const passwordControl = c.get('password');
  const confirmPasswordControl = c.get('confirmPassword');
  let isValid = false;
  if (passwordControl.pristine || confirmPasswordControl.pristine) {
    return null;
  }
  isValid = passwordControl.value === confirmPasswordControl.value;
  if (isValid) {
    return null;
  } else {
    return {'match': false};
  }
}

/**
 * 
 * 
 * @TODO: 
 * 1. add password strength
 * 1. Remove console logs
 */

@Component({
  selector: 'corporate-register',
  templateUrl: 'corporate-register.component.html'

})
export class CorporateRegisterComponent implements OnInit, AfterViewInit {

  confirmPassword: string;
  doNotMatch: string;
  error: string;
  errorEmailExists: string;
  errorUserExists: string;
  registerAccount: any;
  success: boolean;
  corporateRegisterForm: FormGroup;
  passwordMessage: string;
  countries: Country[];
  errorMessage: string;
  componentName: 'CorporateResgister';

  constructor(
    private languageService: JhiLanguageService,
    private registerService: Register,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private spinnerService: NgxSpinnerService
  ) {

  }



  ngOnInit() {
    this.route.data.subscribe((data: {countries: Country[]}) => this.countries = data.countries);

    this.corporateRegisterForm = this.formBuilder.group({

      companyName: [null, [Validators.required, Validators.minLength(5)]],
      phoneNumber: [null, [Validators.required, Validators.pattern('[+.0-9]+'), Validators.minLength(13), Validators.maxLength(13)]],
      email: [null, [Validators.required, Validators.pattern('[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$')]],
      passwordGroup: this.formBuilder.group({
        password: [null, [Validators.required, Validators.minLength(8)]],
        confirmPassword: [null, [Validators.required, Validators.minLength(8)]]
      }, {validator: passwordMatcher}),
      country: [null],
      callingCode: null

    })
    console.log(this.corporateRegisterForm.get('phoneNumber').errors);
    this.corporateRegisterForm.get('country').valueChanges.
      subscribe(value => this.onCountryChange((value)));



    this.success = false;
    this.registerAccount = {authorities: ['ROLE_CORPORATE']};
  }

  onCountryChange(countrySelected) {
    if (countrySelected) {
      if (countrySelected.length === 0) {
        this.corporateRegisterForm.get('phoneNumber').reset();
      }
    }
    for (const country of this.countries) {
      if (country.countryNiceName === countrySelected) {
        this.corporateRegisterForm.get('phoneNumber').setValue('+' + country.phoneCode);
      }
    }

  }

  ngAfterViewInit() {

  }

  register() {
    this.error = null;
    this.errorUserExists = null;
    this.errorEmailExists = null;
    this.languageService.getCurrent().then((key) => {
      this.registerAccount.langKey = key;

      this.registerAccount.companyName = this.corporateRegisterForm.get('companyName').value;
      this.registerAccount.phoneNumber = this.corporateRegisterForm.get('phoneNumber').value;
      this.registerAccount.password = this.corporateRegisterForm.get('passwordGroup.password').value;
      this.registerAccount.email = this.corporateRegisterForm.get('email').value;
      this.registerAccount.login = this.corporateRegisterForm.get('email').value;
      this.registerAccount.country = this.corporateRegisterForm.get('country').value;
      this.spinnerService.show();
      this.registerService.save(this.registerAccount).subscribe(() => {
        this.success = true;
        this.corporateRegisterForm.reset();
        this.spinnerService.hide();
      }, (response) => this.processError(response));
    });

  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    this.spinnerService.hide();
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = 'ERROR';
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = 'ERROR';
    } else {
      this.error = 'ERROR';
    }
  }
}

