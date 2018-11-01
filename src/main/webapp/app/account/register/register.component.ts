import {Component, OnInit, AfterViewInit, Renderer, ElementRef} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiLanguageService} from 'ng-jhipster';
import {HttpErrorResponse} from '@angular/common/http';
import {FormGroup, FormBuilder, Validators, AbstractControl, ValidatorFn, FormArray} from '@angular/forms';
import {Register} from './register.service';
import 'rxjs/add/operator/debounceTime';
import {LoginModalService, EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE} from '../../shared';

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
    private formBuilder: FormBuilder

  ) {
  }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      login: ['', [Validators.required, Validators.minLength(6)]],
      email: ['', [Validators.required, Validators.pattern('[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+')]],
      passwordGroup: this.formBuilder.group({
        password: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', [Validators.required, Validators.minLength(6)]]
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
    // if (this.registerAccount.password !== this.confirmPassword) {
    //     this.doNotMatch = 'ERROR';
    // } else {
    //     this.doNotMatch = null;
    this.error = null;
    this.errorUserExists = null;
    this.errorEmailExists = null;
    this.languageService.getCurrent().then((key) => {
      this.registerAccount.langKey = key;
      this.registerAccount.login = this.registerForm.get('login').value;
      this.registerAccount.password = this.registerForm.get('passwordGroup.password').value;
      this.registerAccount.email = this.registerForm.get('email').value;

      this.registerService.save(this.registerAccount).subscribe(() => {
        this.success = true;
        this.registerForm.reset();
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
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = 'ERROR';
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = 'ERROR';
    } else {
      this.error = 'ERROR';
    }
  }
}
