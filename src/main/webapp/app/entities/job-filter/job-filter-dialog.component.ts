import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { JobFilter } from './job-filter.model';
import { JobFilterPopupService } from './job-filter-popup.service';
import { JobFilterService } from './job-filter.service';
import { Job, JobService } from '../job';

@Component({
    selector: 'jhi-job-filter-dialog',
    templateUrl: './job-filter-dialog.component.html'
})
export class JobFilterDialogComponent implements OnInit {

    jobFilter: JobFilter;
    isSaving: boolean;

    jobs: Job[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private jobFilterService: JobFilterService,
        private jobService: JobService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.jobService.query()
            .subscribe((res: HttpResponse<Job[]>) => { this.jobs = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.jobFilter.id !== undefined) {
            this.subscribeToSaveResponse(
                this.jobFilterService.update(this.jobFilter));
        } else {
            this.subscribeToSaveResponse(
                this.jobFilterService.create(this.jobFilter));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<JobFilter>>) {
        result.subscribe((res: HttpResponse<JobFilter>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: JobFilter) {
        this.eventManager.broadcast({ name: 'jobFilterListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackJobById(index: number, item: Job) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-job-filter-popup',
    template: ''
})
export class JobFilterPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobFilterPopupService: JobFilterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.jobFilterPopupService
                    .open(JobFilterDialogComponent as Component, params['id']);
            } else {
                this.jobFilterPopupService
                    .open(JobFilterDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
