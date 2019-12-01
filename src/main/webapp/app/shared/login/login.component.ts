import { Principal } from '../../core/auth/principal.service';
import { StateStorageService } from '../../core/auth/state-storage.service';
import { LoginService } from '../../core/login/login.service';
import { Component, OnInit, AfterViewInit, Renderer, ElementRef } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { Subscription } from 'rxjs';

@Component({
    selector: 'jhi-login-modal',
    templateUrl: './login.component.html'
})
export class JhiLoginModalComponent implements OnInit, AfterViewInit {
    authenticationError: boolean;
    password: string;
    rememberMe: boolean;
    username: string;
    credentials: any;
    loginForm: FormGroup;
    account: any;
    subscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private loginService: LoginService,
        private stateStorageService: StateStorageService,
        private router: Router,
        private formBuilder: FormBuilder,
        private principal: Principal,
        public activeModal: NgbActiveModal,
        private spinnerService: NgxSpinnerService
    ) {
        this.credentials = {};
    }

    ngOnInit() {
        this.loginForm = this.formBuilder.group({
            login: [null, [Validators.required]],
            password: [null, [Validators.required]],
            rememberMe: true
        });
    }

    ngAfterViewInit() {}

    cancel() {
        this.credentials = {
            username: null,
            password: null,
            rememberMe: true
        };
        this.authenticationError = false;
    }

    login() {
        this.spinnerService.show();
        this.loginService
            .login({
                username: this.loginForm.get('login').value,
                password: this.loginForm.get('password').value,
                rememberMe: this.loginForm.get('rememberMe').value
            })
            .then(data => {
                this.authenticationError = false;

                if (
                    this.router.url === '/register' ||
                    /activate/.test(this.router.url) ||
                    this.router.url === '/finishReset' ||
                    this.router.url === '/requestReset'
                ) {
                    this.router.navigate(['']);
                }

                this.eventManager.broadcast({
                    name: 'authenticationSuccess',
                    content: 'Sending Authentication Success'
                });
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

                this.activeModal.dismiss('to login success');
            })
            .catch(() => {
                this.authenticationError = true;
                this.spinnerService.hide();
            });
    }

    redirectAfterLogin() {
        this.subscriber = this.eventManager.subscribe('userDataLoadedSuccess', response => {
            this.principal.identity().then(value => {
                if (value.authorities.indexOf('ROLE_CANDIDATE') > -1) {
                    this.router.navigate(['/viewjobs']);
                } else if (value.authorities.indexOf('ROLE_CORPORATE') > -1) {
                    this.router.navigate(['/job']);
                } else if (value.authorities.indexOf('ROLE_ADMIN') > -1) {
                    this.router.navigate(['']);
                }
                this.spinnerService.hide();
            });
        });
    }

    register() {
        this.router.navigate(['/register']);
    }

    requestResetPassword() {
        this.activeModal.dismiss('to state requestReset');
        this.router.navigate(['/reset', 'request']);
    }
}
