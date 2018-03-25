import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService,JhiDateUtils } from 'ng-jhipster'
import { CandidateNonAcademicWorkPopupServiceNew } from './candidate-non-academic-work-popup-new.service'
import { CandidateNonAcademicWork } from './candidate-non-academic-work.model';
import { CandidateNonAcademicWorkPopupService } from './candidate-non-academic-work-popup.service';
import { CandidateNonAcademicWorkService } from './candidate-non-academic-work.service';
import { Candidate, CandidateService } from '../candidate';
import { ResponseWrapper, EditorProperties } from '../../shared';


@Component({
    selector: 'jhi-candidate-non-academic-work-dialog',
    templateUrl: './candidate-non-academic-work-dialog.component.html'
})
export class CandidateNonAcademicWorkDialogComponent implements OnInit {

    candidateNonAcademicWork: CandidateNonAcademicWork;
    isSaving: boolean;
    authorities: any[];
    options: Object;
    endDateLesser: boolean;
    endDateControl:boolean;

    candidates: Candidate[];
    nonAcademicWorkStartDateDp: any;
    nonAcademicWorkEndDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private candidateNonAcademicWorkService: CandidateNonAcademicWorkService,
        private candidateService: CandidateService,
        private eventManager: JhiEventManager,
        private dateUtils: JhiDateUtils

    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.options = new EditorProperties().options;

        this.candidateService.query()
            .subscribe((res: ResponseWrapper) => { this.candidates = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    validateDates() {
        this.endDateLesser = false;
       if(this.candidateNonAcademicWork.nonAcademicWorkStartDate && this.candidateNonAcademicWork.nonAcademicWorkEndDate){
       let fromDate = new Date (this.dateUtils
            .convertLocalDateToServer(this.candidateNonAcademicWork.nonAcademicWorkStartDate));
       let toDate = new Date(this.dateUtils
            .convertLocalDateToServer(this.candidateNonAcademicWork.nonAcademicWorkEndDate));

       if(fromDate > toDate )
           this.endDateLesser = true;
       else
           this.endDateLesser = false;
       }


   }

   manageEndDateControl() {
    if (this.candidateNonAcademicWork.isCurrentActivity) {
        this.endDateControl = true;
        this.candidateNonAcademicWork.nonAcademicWorkEndDate='';
        this.endDateLesser =false;
    } else {
        this.endDateControl = false;

    }
}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.candidateNonAcademicWork.id !== undefined) {
            this.subscribeToSaveResponse(
                this.candidateNonAcademicWorkService.update(this.candidateNonAcademicWork));
        } else {
            this.subscribeToSaveResponse(
                this.candidateNonAcademicWorkService.create(this.candidateNonAcademicWork));
        }
    }

    private subscribeToSaveResponse(result: Observable<CandidateNonAcademicWork>) {
        result.subscribe((res: CandidateNonAcademicWork) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: CandidateNonAcademicWork) {
        this.eventManager.broadcast({ name: 'candidateNonAcademicWorkListModification', content: 'OK'});
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
    selector: 'jhi-candidate-non-academic-work-popup',
    template: ''
})
export class CandidateNonAcademicWorkPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateNonAcademicWorkPopupService: CandidateNonAcademicWorkPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.candidateNonAcademicWorkPopupService
                    .open(CandidateNonAcademicWorkDialogComponent as Component, params['id']);
            } else {
                this.candidateNonAcademicWorkPopupService
                    .open(CandidateNonAcademicWorkDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
@Component({
    selector: 'jhi-candidate-non-academic-work-popup',
    template: ''
})
export class CandidateNonAcademicWorkPopupComponentNew implements OnInit, OnDestroy {


    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateNonAcademicWorkPopupService: CandidateNonAcademicWorkPopupServiceNew
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
                this.candidateNonAcademicWorkPopupService
                    .open(CandidateNonAcademicWorkDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
