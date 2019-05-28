import {Component, OnInit, AfterViewInit, Renderer, ElementRef} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiLanguageService} from 'ng-jhipster';
import {HttpErrorResponse} from '@angular/common/http';
import {FormGroup, FormBuilder, Validators, AbstractControl, ValidatorFn, FormArray} from '@angular/forms';
import {Register} from './register.service';
import 'rxjs/add/operator/debounceTime';
import {LoginModalService, EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE} from '../../shared';
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
  return {'match': true};
}


@Component({
  selector: 'jhi-register',
  templateUrl: './register.component.html',
  styleUrls: ['register.css']
})
export class RegisterComponent implements OnInit, AfterViewInit {

  confirmPassword: string;
  doNotMatch: string;
  error: string;
  errorEmailExists: string;
  errorUserExists: string;
  registerAccount: any;
  success: boolean;
  modalRef: NgbModalRef;
  registerForm: FormGroup;
  passwordMessage: string;

  private validationMessages = {
    required: 'Please enter your password.',
    minlength: 'Password needs to be atleast 8 characters.'
  };

  constructor(
    private languageService: JhiLanguageService,
    private loginModalService: LoginModalService,
    private registerService: Register,
    private elementRef: ElementRef,
    private renderer: Renderer,
    private formBuilder: FormBuilder,
    private spinnerService: NgxSpinnerService

  ) {
  }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
    /*  firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],*/
      email: ['', [Validators.required, Validators.pattern('[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+')]],
      passwordGroup: this.formBuilder.group({
        password: ['', [Validators.required, Validators.minLength(8)]],
        confirmPassword: ['', [Validators.required, Validators.minLength(8)]]
      }, {validator: passwordMatcher})

    });

    const passwordControl = this.registerForm.get('passwordGroup.password');
    passwordControl.valueChanges.debounceTime(1000);
    this.registerAccount = {authorities: ['ROLE_CANDIDATE']};         //WHY AM I DOING THIS
    this.success = false;
    // this.registerAccount = {};
  }

  ngAfterViewInit() {
    // this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#login'), 'focus', []);
  }

  register() {
   
    this.error = null;
    this.errorUserExists = null;
    this.errorEmailExists = null;
    this.languageService.getCurrent().then((key) => {
      this.registerAccount.langKey = key;
      //USE email as login id
      this.registerAccount.login = this.registerForm.get('email').value;
      this.registerAccount.password = this.registerForm.get('passwordGroup.password').value;
      this.registerAccount.email = this.registerForm.get('email').value;
    //  this.registerAccount.firstName = this.registerForm.get('firstName').value;
   //   this.registerAccount.lastName = this.registerForm.get('lastName').value;
      this.spinnerService.show();
      this.registerService.save(this.registerAccount).subscribe(() => {
        this.success = true;
        this.registerForm.reset();
        this.spinnerService.hide();
      }, (response) => this.processError(response));
    });
  }

  

  // openLogin() {
  //     this.modalRef = this.loginModalService.open();
  // }
  setMessage(c: AbstractControl): void {
    this.passwordMessage = '';
    //console.log ("-----"+Object.keys(c.errors));
    if ((c.touched || c.dirty) && c.errors) {
      this.passwordMessage = Object.keys(c.errors).map(key =>
        this.validationMessages[key]).join(' ');
    }
    //console.log("Password message is "+this.passwordMessage);
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
