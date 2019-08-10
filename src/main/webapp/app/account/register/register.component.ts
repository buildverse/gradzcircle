import {Component, OnInit, AfterViewInit} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiLanguageService} from 'ng-jhipster';
import {HttpErrorResponse} from '@angular/common/http';
import {FormGroup, FormBuilder, Validators, AbstractControl} from '@angular/forms';
import {Register} from './register.service';
import 'rxjs/add/operator/debounceTime';
import {EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE} from '../../shared';
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


@Component({
  selector: 'jhi-register',
  templateUrl: './register.component.html'

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
    private registerService: Register,
    private formBuilder: FormBuilder,
    private spinnerService: NgxSpinnerService

  ) {
  }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.pattern('[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$')]],
      passwordGroup: this.formBuilder.group({
        password: ['', [Validators.required, Validators.minLength(8)]],
        confirmPassword: ['', [Validators.required, Validators.minLength(8)]]
      }, {validator: passwordMatcher})

    });
    const passwordControl = this.registerForm.get('passwordGroup.password');
    passwordControl.valueChanges.debounceTime(1000);
    this.registerAccount = {authorities: ['ROLE_CANDIDATE']};         //WHY AM I DOING THIS
    this.success = false;
  }

  ngAfterViewInit() {

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
      this.spinnerService.show();
      this.registerService.save(this.registerAccount).subscribe(() => {
        this.success = true;
        this.registerForm.reset();
        this.spinnerService.hide();
      }, (response) => this.processError(response));
    });
  }




  setMessage(c: AbstractControl): void {
    this.passwordMessage = '';

    if ((c.touched || c.dirty) && c.errors) {
      this.passwordMessage = Object.keys(c.errors).map(key =>
        this.validationMessages[key]).join(' ');
    }

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
