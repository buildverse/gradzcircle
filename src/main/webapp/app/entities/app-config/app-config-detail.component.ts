import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { AppConfig } from './app-config.model';
import { AppConfigService } from './app-config.service';

@Component({
    selector: 'jhi-app-config-detail',
    templateUrl: './app-config-detail.component.html'
})
export class AppConfigDetailComponent implements OnInit, OnDestroy {

    appConfig: AppConfig;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private appConfigService: AppConfigService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAppConfigs();
    }

    load(id) {
        this.appConfigService.find(id)
            .subscribe((appConfigResponse: HttpResponse<AppConfig>) => {
                this.appConfig = appConfigResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAppConfigs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'appConfigListModification',
            (response) => this.load(this.appConfig.id)
        );
    }
}
