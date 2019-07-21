import { Component, OnInit, AfterViewInit} from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms'; 
import { JhiLanguageService } from 'ng-jhipster';
import 'rxjs/add/operator/debounceTime';
import { Register } from '../register/register.service';
import { Country } from '../../entities/country/country.model';
import { ActivatedRoute  } from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';


function passwordMatcher(c: AbstractControl): {[key: string]: boolean} | null {
    let passwordControl = c.get('password');
    let confirmPasswordControl = c.get('confirmPassword');
    if (passwordControl.pristine || confirmPasswordControl.pristine) {
      return null;
    }

    if (passwordControl.value === confirmPasswordControl.value) {
        return null;
    }
    return { 'match': true };
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
    templateUrl: 'corporate-register.component.html',
    styleUrls : ['register.css']
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
 //   validationMessages : Map<String,String>;
   // messages : {[id: string]: string;} = {};
    

    //  private validationMessages = {
    //     required: 'Please enter your password.',
    //     minlength: 'Password needs to be atleast 8 characters.'
    // };

    constructor(
        private languageService: JhiLanguageService,
        private registerService: Register,
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private spinnerService: NgxSpinnerService
        
    ) {
        //this.languageService(['register']);
    }

    // setValidationMessages (validationMessageArray: ErrorMessages[]): void {
    //     this.validationMessages = new Map<String,String>();
    //     for(let i =0 ; i < validationMessageArray.length; i++)
    //         //this.validationMessages.set(validationMessageArray[i].errorKey,validationMessageArray[i].errorMessage);
    //         this.messages.push(validationMessageArray[i].errorKey,validationMessageArray[i].errorMessage);
    //     console.log("messages are "+ JSON.stringify(this.validationMessages));
        
    // }res: HttpResponse<Filter[]

    ngOnInit() {
       this.route.data.subscribe((data: {countries: Country[]}) => this.countries = data.countries);
     //  this.route.data.subscribe((data:{validationMessages: ErrorMessages[]})=> this.setValidationMessages(data.validationMessages));
     //  console.log("Error messages "+ JSON.stringify(this.validationMessages));
       this.corporateRegisterForm = this.formBuilder.group({
          //  firstName : [null,[Validators.required]],
            //lastName : [null,[Validators.required]],
            companyName : [null,[Validators.required, Validators.minLength(5)]], 
            phoneNumber : [null,[Validators.required, Validators.pattern('[+.0-9]+'), Validators.minLength(13),Validators.maxLength(13)]],
            email : [null, [Validators.required, Validators.pattern('[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$')]],
            passwordGroup: this.formBuilder.group({
                    password :[null,[Validators.required, Validators.minLength(8)]],
                    confirmPassword : [null,[Validators.required, Validators.minLength(8)]]
            },{validator:passwordMatcher}),
            country : [null],
            callingCode: null
           
        })
          console.log(this.corporateRegisterForm.get('phoneNumber').errors);
          this.corporateRegisterForm.get('country').valueChanges.
              subscribe(value=>this.onCountryChange((value)));

        //Subscribe to chnages and validate fields
        // const firstNameControl = this.corporateRegisterForm.get('firstName');
        // firstNameControl.valueChanges.debounceTime(2000).subscribe(value=>this.setFirstNameMessage(firstNameControl));

        // const lastNameControl = this.corporateRegisterForm.get('lastName');
        // lastNameControl.valueChanges.debounceTime(2000).subscribe(value=>this.setLastNameMessage(lastNameControl));

        // const companyNameControl = this.corporateRegisterForm.get('companyName');
        // companyNameControl.valueChanges.debounceTime(2000).subscribe(value=>this.setCompanyNameMessage(companyNameControl));

        // const emailControl = this.corporateRegisterForm.get('email');
        // emailControl.valueChanges.debounceTime(2000).subscribe(value=>this.setEmailMessage(emailControl));

        // const phoneControl = this.corporateRegisterForm.get('phoneNumber');
        // phoneControl.valueChanges.debounceTime(2000).subscribe(value=>this.setPhoneMessage(phoneControl));

        // const passwordControl = this.corporateRegisterForm.get('passwordGroup.password');
        // passwordControl.valueChanges.debounceTime(1000).subscribe(value=>this.setPasswordMessage(passwordControl));

        // const confirmPasswordControl = this.corporateRegisterForm.get('passwordGroup.confirmPassword');
        // confirmPasswordControl.valueChanges.debounceTime(1000).subscribe(value=>this.setPasswordMessage(confirmPasswordControl));

        this.success = false;
        this.registerAccount = {authorities:['ROLE_CORPORATE']};
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
       // this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#firstname'), 'focus', []);
        //this.onCountryChange (this.countryMap.get(this.country))
    }

    register() {
      this.error = null;
      this.errorUserExists = null;
      this.errorEmailExists = null;
      this.languageService.getCurrent().then((key) => {
        this.registerAccount.langKey = key;
      //  this.registerAccount.firstName = this.corporateRegisterForm.get('firstName').value;
     //   this.registerAccount.lastName = this.corporateRegisterForm.get('lastName').value;
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

 
    // setPasswordMessage(c: AbstractControl): void {
    //     this.passwordMessage = '';
    //     console.log("KEYS ---"+this.validationMessages.keys.name);
    //  //   let passwordValidationMessages = this.validationMessages.k
    //     if ((c.touched || c.dirty) && c.errors) {
    //          console.log ("-----"+JSON.stringify(c.errors));
    //         this.passwordMessage = Object.keys(c.errors).map(key =>
    //             this.validationMessages[key.split(".",1)[1]]).join(' ');
            
    //     }
    //     console.log("Password message is "+this.passwordMessage);
    // }



    private processError(response) {
      this.success = null;
      this.spinnerService.hide(); 
        if (response.status === 400 && response._body === 'login already in use') {
          this.errorUserExists = 'ERROR';
        } else if (response.status === 400 && response._body === 'email address already in use') {
          this.errorEmailExists = 'ERROR';
        } else {
        this.error = 'ERROR';
      }
    }
}

