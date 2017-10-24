import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { JobType } from './job-type.model';
import { JobTypePopupService } from './job-type-popup.service';
import { JobTypeService } from './job-type.service';

@Component({
    selector: 'jhi-job-type-dialog',
    templateUrl: './job-type-dialog.component.html'
})
export class JobTypeDialogComponent implements OnInit {

    jobType: JobType;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private jobTypeService: JobTypeService,
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
        if (this.jobType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.jobTypeService.update(this.jobType));
        } else {
            this.subscribeToSaveResponse(
                this.jobTypeService.create(this.jobType));
        }
    }

    private subscribeToSaveResponse(result: Observable<JobType>) {
        result.subscribe((res: JobType) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: JobType) {
        this.eventManager.broadcast({ name: 'jobTypeListModification', content: 'OK'});
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
    selector: 'jhi-job-type-popup',
    template: ''
})
export class JobTypePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobTypePopupService: JobTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.jobTypePopupService
                    .open(JobTypeDialogComponent as Component, params['id']);
            } else {
                this.jobTypePopupService
                    .open(JobTypeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
