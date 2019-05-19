import { CandidateService } from '../../entities/candidate';
import { CorporateService } from '../../entities/corporate';
import {Component, OnInit, AfterViewInit, Renderer, ElementRef} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {Router} from '@angular/router';
import {JhiLanguageService, JhiEventManager} from 'ng-jhipster';
import {FormGroup, FormBuilder, Validators, AbstractControl, ValidatorFn, FormArray} from '@angular/forms';
import {NgxSpinnerService} from 'ngx-spinner';
import {LoginService} from './login.service';
import {StateStorageService} from '../../shared/auth/state-storage.service';
import {SocialService} from '../../shared/social/social.service';
import {Principal} from '../../shared/auth/principal.service';
import { AuthoritiesConstants } from '../authorities.constant';
import { USER_TYPE, USER_ID, USER_DATA } from '../constants/storage.constants';
import { DataStorageService } from '../helper/localstorage.service';
import { LoginEmitterService } from './login-emitter.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-login-modal',
  templateUrl: './login.component.html',
  styleUrls: ['login.css']
})
export class JhiLoginModalComponent implements OnInit, AfterViewInit {
  authenticationError: boolean;
  password: string;
  rememberMe: boolean;
  username: string;
  credentials: any;
  loginForm: FormGroup;
  account: any;
  subscriber : Subscription;



  constructor(
    private eventManager: JhiEventManager,
    private languageService: JhiLanguageService,
    private loginService: LoginService,
    private stateStorageService: StateStorageService,
    private elementRef: ElementRef,
    private renderer: Renderer,
    private socialService: SocialService,
    private router: Router,
    private formBuilder: FormBuilder,
    private principal: Principal,
    public activeModal: NgbActiveModal,
    private spinnerService: NgxSpinnerService,
    private loginEmitterService: LoginEmitterService,
  ) {
    this.credentials = {};
  }

  ngOnInit() {
    //this.languageService.setLocations(['global','login']);

    this.loginForm = this.formBuilder.group({
      login: [null, [Validators.required]],
      password: [null, [Validators.required]],
      rememberMe: true
    });

  }

  ngAfterViewInit() {
    // this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#username'), 'focus', []);
  }

  cancel() {
    this.credentials = {
      username: null,
      password: null,
      rememberMe: true
    };
    this.authenticationError = false;
    // this.activeModal.dismiss('cancel');
  }

  login() {
    this.spinnerService.show();
    this.loginService.login({
      username: this.loginForm.get('login').value,
      password: this.loginForm.get('password').value,
      rememberMe: this.loginForm.get('rememberMe').value

    }).then((data) => {
      this.authenticationError = false;
      //  this.activeModal.dismiss('login success');
      if (this.router.url === '/register' || (/activate/.test(this.router.url)) ||
        this.router.url === '/finishReset' || this.router.url === '/requestReset') {
        this.router.navigate(['']);
      }

      this.eventManager.broadcast({
        name: 'authenticationSuccess',
        content: 'Sending Authentication Success'
      });
      this.loginEmitterService.loginSuccess(true);
      // // previousState was set in the authExpiredInterceptor before being redirected to login modal.
      // // since login is succesful, go to stored previousState and clear previousState
      const redirect = this.stateStorageService.getUrl();
      // console.log("Redirecting to "+ redirect);
      if (!redirect) {
        this.redirectAfterLogin();
      }
      if (redirect) {
        this.stateStorageService.storeUrl(null);
        this.router.navigate([redirect]);
      }
      this.spinnerService.hide();
      this.activeModal.dismiss('to login success');
    }).catch(() => {
      this.authenticationError = true;
      this.spinnerService.hide();
    });//

    //   this.redirectAfterLogin ();
   
  }

  redirectAfterLogin() {
    const redirectTo = null;
    this.subscriber = this.eventManager.subscribe('userDataLoadedSuccess', (response) => {
         console.log('Navigate after subscirber retruens');
      this.principal.identity().then((value) => {
        // this.loadId();
        if (value.authorities.indexOf('ROLE_CANDIDATE') > -1) {
          //this.router.navigate(['/candidate-profile',{outlets:{detail:'details'}}]);
          this.router.navigate(['/viewjobs']);
        } else if (value.authorities.indexOf('ROLE_CORPORATE') > -1) {
          this.router.navigate(['/job']);
        } else if (value.authorities.indexOf('ROLE_ADMIN') > -1) {
          this.router.navigate(['']);
        }
      });
    });

  }
  
 /*
*/
  register() {
    //  this.activeModal.dismiss('to state register');
    this.router.navigate(['/register']);
  }

  requestResetPassword() {
    this.activeModal.dismiss('to state requestReset');
    this.router.navigate(['/reset', 'request']);
  }
}
