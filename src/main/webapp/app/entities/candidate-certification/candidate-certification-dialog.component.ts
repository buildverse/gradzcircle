import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CandidateCertification } from './candidate-certification.model';
import { CandidateCertificationPopupService } from './candidate-certification-popup.service';
import { CandidateCertificationService } from './candidate-certification.service';
import { Candidate, CandidateService } from '../candidate';
import { CandidateCertificationPopupServiceNew } from './candidate-certification-popup-new.service';
import { ResponseWrapper,EditorProperties } from '../../shared';

@Component({
    selector: 'jhi-candidate-certification-dialog',
    templateUrl: './candidate-certification-dialog.component.html'
})
export class CandidateCertificationDialogComponent implements OnInit {

    candidateCertification: CandidateCertification;
    isSaving: boolean;
    authorities: any[];
    candidates: Candidate[];
    certificationDateDp: any;
    options : Object;
    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private candidateCertificationService: CandidateCertificationService,
        private candidateService: CandidateService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
                this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.options = new EditorProperties().options;
        this.candidateService.query()
            .subscribe((res: ResponseWrapper) => { this.candidates = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.candidateCertification.id !== undefined) {
            this.subscribeToSaveResponse(
                this.candidateCertificationService.update(this.candidateCertification));
        } else {
            this.subscribeToSaveResponse(
                this.candidateCertificationService.create(this.candidateCertification));
        }
    }

    private subscribeToSaveResponse(result: Observable<CandidateCertification>) {
        result.subscribe((res: CandidateCertification) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: CandidateCertification) {
        this.eventManager.broadcast({ name: 'candidateCertificationListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-candidate-certification-popup',
    template: ''
})
export class CandidateCertificationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateCertificationPopupService: CandidateCertificationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.candidateCertificationPopupService
                    .open(CandidateCertificationDialogComponent as Component, params['id']);
            } else {
                this.candidateCertificationPopupService
                    .open(CandidateCertificationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

@Component({
    selector: 'jhi-candidate-certification-popup',
    template: ''
})
export class CandidateCertificationPopupComponentNew implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateCertificationPopupService: CandidateCertificationPopupServiceNew
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
                this.candidateCertificationPopupService
                    .open(CandidateCertificationDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
