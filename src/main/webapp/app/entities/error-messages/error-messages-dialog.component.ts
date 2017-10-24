import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ErrorMessages } from './error-messages.model';
import { ErrorMessagesPopupService } from './error-messages-popup.service';
import { ErrorMessagesService } from './error-messages.service';

@Component({
    selector: 'jhi-error-messages-dialog',
    templateUrl: './error-messages-dialog.component.html'
})
export class ErrorMessagesDialogComponent implements OnInit {

    errorMessages: ErrorMessages;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private errorMessagesService: ErrorMessagesService,
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
        if (this.errorMessages.id !== undefined) {
            this.subscribeToSaveResponse(
                this.errorMessagesService.update(this.errorMessages));
        } else {
            this.subscribeToSaveResponse(
                this.errorMessagesService.create(this.errorMessages));
        }
    }

    private subscribeToSaveResponse(result: Observable<ErrorMessages>) {
        result.subscribe((res: ErrorMessages) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ErrorMessages) {
        this.eventManager.broadcast({ name: 'errorMessagesListModification', content: 'OK'});
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
    selector: 'jhi-error-messages-popup',
    template: ''
})
export class ErrorMessagesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private errorMessagesPopupService: ErrorMessagesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.errorMessagesPopupService
                    .open(ErrorMessagesDialogComponent as Component, params['id']);
            } else {
                this.errorMessagesPopupService
                    .open(ErrorMessagesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
