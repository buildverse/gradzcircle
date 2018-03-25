import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { JobFilterHistory } from './job-filter-history.model';
import { JobFilterHistoryPopupService } from './job-filter-history-popup.service';
import { JobFilterHistoryService } from './job-filter-history.service';
import { JobFilter, JobFilterService } from '../job-filter';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-job-filter-history-dialog',
    templateUrl: './job-filter-history-dialog.component.html'
})
export class JobFilterHistoryDialogComponent implements OnInit {

    jobFilterHistory: JobFilterHistory;
    isSaving: boolean;

    jobfilters: JobFilter[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private jobFilterHistoryService: JobFilterHistoryService,
        private jobFilterService: JobFilterService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.jobFilterService.query()
            .subscribe((res: ResponseWrapper) => { this.jobfilters = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.jobFilterHistory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.jobFilterHistoryService.update(this.jobFilterHistory));
        } else {
            this.subscribeToSaveResponse(
                this.jobFilterHistoryService.create(this.jobFilterHistory));
        }
    }

    private subscribeToSaveResponse(result: Observable<JobFilterHistory>) {
        result.subscribe((res: JobFilterHistory) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: JobFilterHistory) {
        this.eventManager.broadcast({ name: 'jobFilterHistoryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackJobFilterById(index: number, item: JobFilter) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-job-filter-history-popup',
    template: ''
})
export class JobFilterHistoryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobFilterHistoryPopupService: JobFilterHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.jobFilterHistoryPopupService
                    .open(JobFilterHistoryDialogComponent as Component, params['id']);
            } else {
                this.jobFilterHistoryPopupService
                    .open(JobFilterHistoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
