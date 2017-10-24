import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Qualification } from './qualification.model';
import { QualificationPopupService } from './qualification-popup.service';
import { QualificationService } from './qualification.service';

@Component({
    selector: 'jhi-qualification-dialog',
    templateUrl: './qualification-dialog.component.html'
})
export class QualificationDialogComponent implements OnInit {

    qualification: Qualification;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private qualificationService: QualificationService,
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
        if (this.qualification.id !== undefined) {
            this.subscribeToSaveResponse(
                this.qualificationService.update(this.qualification));
        } else {
            this.subscribeToSaveResponse(
                this.qualificationService.create(this.qualification));
        }
    }

    private subscribeToSaveResponse(result: Observable<Qualification>) {
        result.subscribe((res: Qualification) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Qualification) {
        this.eventManager.broadcast({ name: 'qualificationListModification', content: 'OK'});
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
    selector: 'jhi-qualification-popup',
    template: ''
})
export class QualificationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private qualificationPopupService: QualificationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.qualificationPopupService
                    .open(QualificationDialogComponent as Component, params['id']);
            } else {
                this.qualificationPopupService
                    .open(QualificationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
