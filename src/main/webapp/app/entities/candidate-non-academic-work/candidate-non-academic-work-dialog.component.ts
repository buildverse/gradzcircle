import { DATE_FORMAT } from '../../shared/constants/input.constants';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { NgxSpinnerService } from 'ngx-spinner';
import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { CandidateNonAcademicWorkPopupServiceNew } from './candidate-non-academic-work-popup-new.service';
import { CandidateNonAcademicWork } from './candidate-non-academic-work.model';
import { CandidateNonAcademicWorkPopupService } from './candidate-non-academic-work-popup.service';
import { CandidateNonAcademicWorkService } from './candidate-non-academic-work.service';
import { Candidate, CandidateService } from '../candidate';
import { CANDIDATE_ID, CANDIDATE_NON_ACADEMIC_ID } from '../../shared/constants/storage.constants';
import { EditorProperties } from '../../shared/editor/editor-properties';
import { DataStorageService } from '../../shared/helper/localstorage.service';

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
    endDateControl: boolean;
    editorConfig: any;
    candidates: Candidate[];
    nonAcademicWorkStartDateDp: any;
    nonAcademicWorkEndDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private candidateNonAcademicWorkService: CandidateNonAcademicWorkService,
        private candidateService: CandidateService,
        private eventManager: JhiEventManager,
        private spinnerService: NgxSpinnerService,
        private config: NgbDatepickerConfig
    ) {}

    configureDatePicker() {
        this.config.minDate = { year: 1980, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };

        // days that don't belong to current month are not visible
        this.config.outsideDays = 'hidden';
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
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.options = new EditorProperties().options;
        this.candidateNonAcademicWork.isCurrentActivity ? (this.endDateControl = true) : (this.endDateControl = false);
        this.candidateService.query().subscribe(
            (res: HttpResponse<Candidate[]>) => {
                this.candidates = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    validateDates() {
        this.endDateLesser = false;
        if (this.candidateNonAcademicWork.nonAcademicWorkStartDate && this.candidateNonAcademicWork.nonAcademicWorkEndDate) {
            const fromDate = new Date(this.candidateNonAcademicWork.nonAcademicWorkStartDate.format(DATE_FORMAT));
            const toDate = new Date(this.candidateNonAcademicWork.nonAcademicWorkEndDate.format(DATE_FORMAT));

            if (fromDate > toDate) {
                this.endDateLesser = true;
            } else {
                this.endDateLesser = false;
            }
        }
    }

    manageEndDateControl() {
        if (this.candidateNonAcademicWork.isCurrentActivity) {
            this.endDateControl = true;
            this.candidateNonAcademicWork.nonAcademicWorkEndDate = null;
            this.endDateLesser = false;
        } else {
            this.endDateControl = false;
        }
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        this.spinnerService.show();
        if (this.candidateNonAcademicWork.id !== undefined) {
            this.subscribeToSaveResponse(this.candidateNonAcademicWorkService.update(this.candidateNonAcademicWork));
        } else {
            this.subscribeToSaveResponse(this.candidateNonAcademicWorkService.create(this.candidateNonAcademicWork));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<CandidateNonAcademicWork>>) {
        result.subscribe(
            (res: HttpResponse<CandidateNonAcademicWork>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => this.onError(res.error)
        );
    }

    private onSaveSuccess(result: CandidateNonAcademicWork) {
        this.eventManager.broadcast({ name: 'candidateNonAcademicWorkListModification', content: 'OK' });
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
        this.onSaveError();
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
        private candidateNonAcademicWorkPopupService: CandidateNonAcademicWorkPopupService,
        private dataService: DataStorageService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.candidateNonAcademicWorkPopupService.open(CandidateNonAcademicWorkDialogComponent as Component, params['id']);
            } else {
                const id = this.dataService.getData(CANDIDATE_NON_ACADEMIC_ID);
                if (id) {
                    this.candidateNonAcademicWorkPopupService.open(CandidateNonAcademicWorkDialogComponent as Component, id);
                } else {
                    this.candidateNonAcademicWorkPopupService.open(CandidateNonAcademicWorkDialogComponent as Component);
                }
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
export class CandidateNonAcademicWorkPopupNewComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateNonAcademicWorkPopupService: CandidateNonAcademicWorkPopupServiceNew,
        private dataService: DataStorageService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.candidateNonAcademicWorkPopupService.open(CandidateNonAcademicWorkDialogComponent as Component, params['id']);
            } else {
                const id = this.dataService.getData(CANDIDATE_ID);
                this.candidateNonAcademicWorkPopupService.open(CandidateNonAcademicWorkDialogComponent as Component, id);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
