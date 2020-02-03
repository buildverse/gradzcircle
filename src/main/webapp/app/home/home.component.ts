import { AppConfig, AppConfigService } from '../entities/app-config';
import { BUSINESS_PLAN_ENABLED } from '../shared/constants/storage.constants';
import { DataStorageService } from '../shared/helper/localstorage.service';
import { HttpErrorResponse } from '@angular/common/http';
import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

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
    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private localStorageService: DataStorageService,
        private appConfigService: AppConfigService,
        private router: Router
    ) {}

    ngOnInit() {
        // this.localStorageService.setdata(BUSINESS_PLAN_ENABLED, 'false');
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.localStorageService.setdata(BUSINESS_PLAN_ENABLED, false);
        this.appConfigService.query().subscribe(
            (res: HttpResponse<AppConfig[]>) => {
                this.appConfigs = res.body;
                this.appConfigs.forEach(appConfig => {
                    if ('BusinessPlan'.toUpperCase().indexOf(appConfig.configName ? appConfig.configName.toUpperCase() : '') > -1) {
                        this.localStorageService.setdata(BUSINESS_PLAN_ENABLED, appConfig.configValue);
                        //     console.log('biz plab enable is ' + this.localStorageService.getData(BUSINESS_PLAN_ENABLED));
                        // console.log('biz plab enable in home is ' + JSON.parse(this.localStorageService.getData(BUSINESS_PLAN_ENABLED)));
                    }
                });
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
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
