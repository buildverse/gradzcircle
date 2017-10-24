import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CandidateEmployment } from './candidate-employment.model';
import { CandidateEmploymentPopupService } from './candidate-employment-popup.service';
import { CandidateEmploymentPopupServiceNew } from './candidate-employment-popup-new.service';

import { CandidateEmploymentService } from './candidate-employment.service';
import { Candidate, CandidateService } from '../candidate';
import { EmploymentType, EmploymentTypeService } from '../employment-type';
import { Country, CountryService } from '../country';
import { JobType, JobTypeService } from '../job-type';
import { JhiDateUtils } from 'ng-jhipster';
import { ResponseWrapper, EditorProperties } from '../../shared';

@Component({
    selector: 'jhi-candidate-employment-dialog',
    templateUrl: './candidate-employment-dialog.component.html'
})
export class CandidateEmploymentDialogComponent implements OnInit {

    candidateEmployment: CandidateEmployment;
    authorities: any[];
    isSaving: boolean;
    endDateLesser: boolean;
    endDateControl :boolean;
    candidates: Candidate[];
    options : Object;
    employmenttypes: EmploymentType[];

    countries: Country[];

    jobtypes: JobType[];
    employmentStartDateDp: any;
    employmentEndDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private candidateEmploymentService: CandidateEmploymentService,
        private candidateService: CandidateService,
        private employmentTypeService: EmploymentTypeService,
        private countryService: CountryService,
        private jobTypeService: JobTypeService,
        private eventManager: JhiEventManager,
        private dateUtils: JhiDateUtils
    ) {
    }

    manageEndDateControl() {
        if (this.candidateEmployment.isCurrentEmployment) {
            this.endDateControl = true;
            this.candidateEmployment.employmentEndDate='';
            this.endDateLesser =false;
        } else {
            this.endDateControl = false;

        }
    }

    validateDates() {
         this.endDateLesser = false;
        if(this.candidateEmployment.employmentStartDate && this.candidateEmployment.employmentEndDate){
        let fromDate = new Date (this.dateUtils
             .convertLocalDateToServer(this.candidateEmployment.employmentStartDate));
        let toDate = new Date(this.dateUtils
             .convertLocalDateToServer(this.candidateEmployment.employmentEndDate));

        if(fromDate > toDate )
            this.endDateLesser = true;
        else
            this.endDateLesser = false;
        }
    }

    ngOnInit() {
        this.isSaving = false;
        this.endDateLesser = false;
        this.candidateEmployment.isCurrentEmployment?this.endDateControl=true:this.endDateControl=false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.options = new EditorProperties().options;
        this.candidateService.query()
            .subscribe((res: ResponseWrapper) => { this.candidates = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.employmentTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.employmenttypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.countryService.query()
            .subscribe((res: ResponseWrapper) => { this.countries = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.jobTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.jobtypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.candidateEmployment.id !== undefined) {
            this.subscribeToSaveResponse(
                this.candidateEmploymentService.update(this.candidateEmployment));
        } else {
            this.subscribeToSaveResponse(
                this.candidateEmploymentService.create(this.candidateEmployment));
        }
    }

    private subscribeToSaveResponse(result: Observable<CandidateEmployment>) {
        result.subscribe((res: CandidateEmployment) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: CandidateEmployment) {
        this.eventManager.broadcast({ name: 'candidateEmploymentListModification', content: 'OK'});
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

    trackEmploymentTypeById(index: number, item: EmploymentType) {
        return item.id;
    }

    trackCountryById(index: number, item: Country) {
        return item.id;
    }

    trackJobTypeById(index: number, item: JobType) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-candidate-employment-popup',
    template: ''
})
export class CandidateEmploymentPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateEmploymentPopupService: CandidateEmploymentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.candidateEmploymentPopupService
                    .open(CandidateEmploymentDialogComponent as Component, params['id']);
            } else {
                this.candidateEmploymentPopupService
                    .open(CandidateEmploymentDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}


@Component({
    selector: 'jhi-candidate-employment-popup',
    template: ''
})
export class CandidateEmploymentPopupComponentNew implements OnInit, OnDestroy {


    routeSub: any;


    constructor(
        private route: ActivatedRoute,
        private candidateEmploymentPopupService: CandidateEmploymentPopupServiceNew,

    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
                this.candidateEmploymentPopupService
                    .open(CandidateEmploymentDialogComponent as Component, params['id']);

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
