import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { AppConfig } from './app-config.model';
import { AppConfigPopupService } from './app-config-popup.service';
import { AppConfigService } from './app-config.service';

@Component({
    selector: 'jhi-app-config-dialog',
    templateUrl: './app-config-dialog.component.html'
})
export class AppConfigDialogComponent implements OnInit {

    appConfig: AppConfig;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private appConfigService: AppConfigService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.appConfig.id !== undefined) {
            this.subscribeToSaveResponse(
                this.appConfigService.update(this.appConfig));
        } else {
            this.subscribeToSaveResponse(
                this.appConfigService.create(this.appConfig));
        }
    }

    private subscribeToSaveResponse(result: Observable<AppConfig>) {
        result.subscribe((res: AppConfig) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: AppConfig) {
        this.eventManager.broadcast({ name: 'appConfigListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-app-config-popup',
    template: ''
})
export class AppConfigPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private appConfigPopupService: AppConfigPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.appConfigPopupService
                    .open(AppConfigDialogComponent as Component, params['id']);
            } else {
                this.appConfigPopupService
                    .open(AppConfigDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
