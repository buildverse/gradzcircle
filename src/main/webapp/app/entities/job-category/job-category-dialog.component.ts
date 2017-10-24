import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { JobCategory } from './job-category.model';
import { JobCategoryPopupService } from './job-category-popup.service';
import { JobCategoryService } from './job-category.service';
import { Candidate, CandidateService } from '../candidate';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-job-category-dialog',
    templateUrl: './job-category-dialog.component.html'
})
export class JobCategoryDialogComponent implements OnInit {

    jobCategory: JobCategory;
    isSaving: boolean;

    candidates: Candidate[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private jobCategoryService: JobCategoryService,
        private candidateService: CandidateService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.candidateService.query()
            .subscribe((res: ResponseWrapper) => { this.candidates = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.jobCategory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.jobCategoryService.update(this.jobCategory));
        } else {
            this.subscribeToSaveResponse(
                this.jobCategoryService.create(this.jobCategory));
        }
    }

    private subscribeToSaveResponse(result: Observable<JobCategory>) {
        result.subscribe((res: JobCategory) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: JobCategory) {
        this.eventManager.broadcast({ name: 'jobCategoryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCandidateById(index: number, item: Candidate) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-job-category-popup',
    template: ''
})
export class JobCategoryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobCategoryPopupService: JobCategoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.jobCategoryPopupService
                    .open(JobCategoryDialogComponent as Component, params['id']);
            } else {
                this.jobCategoryPopupService
                    .open(JobCategoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
