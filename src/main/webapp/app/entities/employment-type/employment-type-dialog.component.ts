import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { EmploymentType } from './employment-type.model';
import { EmploymentTypePopupService } from './employment-type-popup.service';
import { EmploymentTypeService } from './employment-type.service';

@Component({
    selector: 'jhi-employment-type-dialog',
    templateUrl: './employment-type-dialog.component.html'
})
export class EmploymentTypeDialogComponent implements OnInit {

    employmentType: EmploymentType;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private employmentTypeService: EmploymentTypeService,
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
        if (this.employmentType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.employmentTypeService.update(this.employmentType));
        } else {
            this.subscribeToSaveResponse(
                this.employmentTypeService.create(this.employmentType));
        }
    }

    private subscribeToSaveResponse(result: Observable<EmploymentType>) {
        result.subscribe((res: EmploymentType) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: EmploymentType) {
        this.eventManager.broadcast({ name: 'employmentTypeListModification', content: 'OK'});
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
    selector: 'jhi-employment-type-popup',
    template: ''
})
export class EmploymentTypePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private employmentTypePopupService: EmploymentTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.employmentTypePopupService
                    .open(EmploymentTypeDialogComponent as Component, params['id']);
            } else {
                this.employmentTypePopupService
                    .open(EmploymentTypeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
