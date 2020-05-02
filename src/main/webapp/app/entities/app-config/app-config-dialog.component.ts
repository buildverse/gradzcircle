import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

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
        private appConfigService: AppConfigService,
        private eventManager: JhiEventManager,
        private router: Router
    ) {}

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    clearRoute() {
        this.router.navigate(['/admin', { outlets: { popup: null } }]);
    }

    save() {
        this.isSaving = true;
        if (this.appConfig.id !== undefined) {
            this.subscribeToSaveResponse(this.appConfigService.update(this.appConfig));
        } else {
            this.subscribeToSaveResponse(this.appConfigService.create(this.appConfig));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<AppConfig>>) {
        result.subscribe((res: HttpResponse<AppConfig>) => this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: AppConfig) {
        this.eventManager.broadcast({ name: 'appConfigListModification', content: 'OK' });
        this.isSaving = false;
        this.clearRoute();
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-app-config-popup',
    template: ''
})
export class AppConfigPopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private appConfigPopupService: AppConfigPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.appConfigPopupService.open(AppConfigDialogComponent as Component, params['id']);
            } else {
                this.appConfigPopupService.open(AppConfigDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
