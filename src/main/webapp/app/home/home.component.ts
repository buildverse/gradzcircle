import { AppConfig, AppConfigService } from '../entities/app-config';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { AmazonConstants } from '../shared/amazon.constants';

import { LoginModalService, Principal, Account } from 'app/core';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.css']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    appConfigs: AppConfig[];
    logoImage = AmazonConstants.S3_URL + 'logo_blue.png?t=' + Math.random().toString();
    talentImage = AmazonConstants.S3_URL + 'talent.jpg?t=' + Math.random().toString();
    jobImage = AmazonConstants.S3_URL + 'jobs.jpg?t=' + Math.random().toString();
    corporateImage = AmazonConstants.S3_URL + 'corporate.jpg?t=' + Math.random().toString();
    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private router: Router
    ) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    private onError(error: any) {
        this.router.navigate(['/error']);
    }
}
