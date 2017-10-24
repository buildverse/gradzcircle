import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CaptureQualification } from './capture-qualification.model';
import { CaptureQualificationPopupService } from './capture-qualification-popup.service';
import { CaptureQualificationService } from './capture-qualification.service';
import { CandidateEducation, CandidateEducationService } from '../candidate-education';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-capture-qualification-dialog',
    templateUrl: './capture-qualification-dialog.component.html'
})
export class CaptureQualificationDialogComponent implements OnInit {

    captureQualification: CaptureQualification;
    isSaving: boolean;

    candidateeducations: CandidateEducation[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private captureQualificationService: CaptureQualificationService,
        private candidateEducationService: CandidateEducationService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.candidateEducationService
            .query({filter: 'capturequalification-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.captureQualification.candidateEducation || !this.captureQualification.candidateEducation.id) {
                    this.candidateeducations = res.json;
                } else {
                    this.candidateEducationService
                        .find(this.captureQualification.candidateEducation.id)
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
        if (this.captureQualification.id !== undefined) {
            this.subscribeToSaveResponse(
                this.captureQualificationService.update(this.captureQualification));
        } else {
            this.subscribeToSaveResponse(
                this.captureQualificationService.create(this.captureQualification));
        }
    }

    private subscribeToSaveResponse(result: Observable<CaptureQualification>) {
        result.subscribe((res: CaptureQualification) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: CaptureQualification) {
        this.eventManager.broadcast({ name: 'captureQualificationListModification', content: 'OK'});
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
    selector: 'jhi-capture-qualification-popup',
    template: ''
})
export class CaptureQualificationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private captureQualificationPopupService: CaptureQualificationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.captureQualificationPopupService
                    .open(CaptureQualificationDialogComponent as Component, params['id']);
            } else {
                this.captureQualificationPopupService
                    .open(CaptureQualificationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
