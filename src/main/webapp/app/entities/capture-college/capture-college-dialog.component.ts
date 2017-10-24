import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CaptureCollege } from './capture-college.model';
import { CaptureCollegePopupService } from './capture-college-popup.service';
import { CaptureCollegeService } from './capture-college.service';
import { CandidateEducation, CandidateEducationService } from '../candidate-education';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-capture-college-dialog',
    templateUrl: './capture-college-dialog.component.html'
})
export class CaptureCollegeDialogComponent implements OnInit {

    captureCollege: CaptureCollege;
    isSaving: boolean;

    candidateeducations: CandidateEducation[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private captureCollegeService: CaptureCollegeService,
        private candidateEducationService: CandidateEducationService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.candidateEducationService
            .query({filter: 'capturecollege-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.captureCollege.candidateEducation || !this.captureCollege.candidateEducation.id) {
                    this.candidateeducations = res.json;
                } else {
                    this.candidateEducationService
                        .find(this.captureCollege.candidateEducation.id)
                        .subscribe((subRes: CandidateEducation) => {
                            this.candidateeducations = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.captureCollege.id !== undefined) {
            this.subscribeToSaveResponse(
                this.captureCollegeService.update(this.captureCollege));
        } else {
            this.subscribeToSaveResponse(
                this.captureCollegeService.create(this.captureCollege));
        }
    }

    private subscribeToSaveResponse(result: Observable<CaptureCollege>) {
        result.subscribe((res: CaptureCollege) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: CaptureCollege) {
        this.eventManager.broadcast({ name: 'captureCollegeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCandidateEducationById(index: number, item: CandidateEducation) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-capture-college-popup',
    template: ''
})
export class CaptureCollegePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private captureCollegePopupService: CaptureCollegePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.captureCollegePopupService
                    .open(CaptureCollegeDialogComponent as Component, params['id']);
            } else {
                this.captureCollegePopupService
                    .open(CaptureCollegeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
