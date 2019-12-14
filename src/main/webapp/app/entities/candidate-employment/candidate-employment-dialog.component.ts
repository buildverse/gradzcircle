import { DATE_FORMAT } from '../../shared/constants/input.constants';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef, NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { NgxSpinnerService } from 'ngx-spinner';
import { CandidateEmployment } from './candidate-employment.model';
import { CandidateEmploymentPopupService } from './candidate-employment-popup.service';
import { CandidateEmploymentPopupServiceNew } from './candidate-employment-popup-new.service';

import { CandidateEmploymentService } from './candidate-employment.service';
import { Candidate, CandidateService } from '../candidate';
import { EmploymentType, EmploymentTypeService } from '../employment-type';
import { Country, CountryService } from '../country';
import { JobType, JobTypeService } from '../job-type';
import { CANDIDATE_ID, CANDIDATE_EMPLOYMENT_ID } from '../../shared/constants/storage.constants';
import { EditorProperties } from '../../shared/editor/editor-properties';
import { DataStorageService } from '../../shared/helper/localstorage.service';

@Component({
    selector: 'jhi-candidate-employment-dialog',
    templateUrl: './candidate-employment-dialog.component.html'
})
export class CandidateEmploymentDialogComponent implements OnInit {
    candidateEmployment: CandidateEmployment;
    authorities: any[];
    isSaving: boolean;
    endDateLesser: boolean;
    endDateControl: boolean;
    candidates: Candidate[];
    options: Object;
    employmenttypes: EmploymentType[];
    editorConfig: any;
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
        private spinnerService: NgxSpinnerService,
        private config: NgbDatepickerConfig
    ) {}

    manageEndDateControl() {
        if (this.candidateEmployment.isCurrentEmployment) {
            this.endDateControl = true;
            this.candidateEmployment.employmentEndDate = null;
            this.endDateLesser = false;
        } else {
            this.endDateControl = false;
        }
    }

    configureDatePicker() {
        this.config.minDate = { year: 1980, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };

        // days that don't belong to current month are not visible
        this.config.outsideDays = 'hidden';
    }

    validateDates() {
        this.endDateLesser = false;
        if (this.candidateEmployment.employmentStartDate && this.candidateEmployment.employmentEndDate) {
            const fromDate = new Date(this.candidateEmployment.employmentStartDate.format(DATE_FORMAT));
            const toDate = new Date(this.candidateEmployment.employmentEndDate.format(DATE_FORMAT));

            if (fromDate > toDate) {
                this.endDateLesser = true;
            } else {
                this.endDateLesser = false;
            }
        }
    }

    ngOnInit() {
        this.configureDatePicker();
        this.editorConfig = {
            toolbarGroups: [
                { name: 'editing', groups: ['find', 'selection', 'spellchecker', 'editing'] },
                { name: 'basicstyles', groups: ['basicstyles', 'cleanup'] },
                { name: 'paragraph', groups: ['list', 'indent', 'align'] }
            ],
            removeButtons: 'Source,Save,Templates,Find,Replace,Scayt,SelectAll,forms'
        };
        this.isSaving = false;
        this.endDateLesser = false;
        this.candidateEmployment.isCurrentEmployment ? (this.endDateControl = true) : (this.endDateControl = false);
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.options = new EditorProperties().options;
        this.candidateService
            .query()
            .subscribe(
                (res: HttpResponse<Candidate[]>) => (this.candidates = res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.employmentTypeService
            .query()
            .subscribe(
                (res: HttpResponse<EmploymentType[]>) => (this.employmenttypes = res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.countryService
            .query()
            .subscribe(
                (res: HttpResponse<Country[]>) => (this.countries = res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.jobTypeService
            .query()
            .subscribe((res: HttpResponse<JobType[]>) => (this.jobtypes = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        this.spinnerService.show();
        if (this.candidateEmployment.id !== undefined) {
            this.subscribeToSaveResponse(this.candidateEmploymentService.update(this.candidateEmployment));
        } else {
            this.subscribeToSaveResponse(this.candidateEmploymentService.create(this.candidateEmployment));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<CandidateEmployment>>) {
        result.subscribe(
            (res: HttpResponse<CandidateEmployment>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess(result: CandidateEmployment) {
        this.eventManager.broadcast({ name: 'candidateEmploymentListModification', content: 'OK' });
        this.eventManager.broadcast({ name: 'candidateListModification', content: 'OK' });
        this.isSaving = false;
        this.spinnerService.hide();
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
        this.spinnerService.hide();
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
        private candidateEmploymentPopupService: CandidateEmploymentPopupService,
        private dataService: DataStorageService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.candidateEmploymentPopupService.open(CandidateEmploymentDialogComponent as Component, params['id']);
            } else {
                const id = this.dataService.getData(CANDIDATE_EMPLOYMENT_ID);
                if (id) {
                    this.candidateEmploymentPopupService.open(CandidateEmploymentDialogComponent as Component, id);
                } else {
                    this.candidateEmploymentPopupService.open(CandidateEmploymentDialogComponent as Component);
                }
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
export class CandidateEmploymentPopupNewComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateEmploymentPopupService: CandidateEmploymentPopupServiceNew,
        private dataService: DataStorageService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.candidateEmploymentPopupService.open(CandidateEmploymentDialogComponent as Component, params['id']);
            } else {
                const id = this.dataService.getData(CANDIDATE_ID);
                this.candidateEmploymentPopupService.open(CandidateEmploymentDialogComponent as Component, id);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
