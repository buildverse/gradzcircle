import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { MaritalStatus } from './marital-status.model';
import { MaritalStatusPopupService } from './marital-status-popup.service';
import { MaritalStatusService } from './marital-status.service';

@Component({
    selector: 'jhi-marital-status-dialog',
    templateUrl: './marital-status-dialog.component.html'
})
export class MaritalStatusDialogComponent implements OnInit {

    maritalStatus: MaritalStatus;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private maritalStatusService: MaritalStatusService,
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
        if (this.maritalStatus.id !== undefined) {
            this.subscribeToSaveResponse(
                this.maritalStatusService.update(this.maritalStatus));
        } else {
            this.subscribeToSaveResponse(
                this.maritalStatusService.create(this.maritalStatus));
        }
    }

    private subscribeToSaveResponse(result: Observable<MaritalStatus>) {
        result.subscribe((res: MaritalStatus) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: MaritalStatus) {
        this.eventManager.broadcast({ name: 'maritalStatusListModification', content: 'OK'});
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
    selector: 'jhi-marital-status-popup',
    template: ''
})
export class MaritalStatusPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private maritalStatusPopupService: MaritalStatusPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.maritalStatusPopupService
                    .open(MaritalStatusDialogComponent as Component, params['id']);
            } else {
                this.maritalStatusPopupService
                    .open(MaritalStatusDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
