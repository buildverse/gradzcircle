import { DATE_FORMAT } from '../../shared/constants/input.constants';
import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { CandidateProject } from './candidate-project.model';
import { CandidateProjectPopupService } from './candidate-project-popup.service';
import { CandidateProjectService } from './candidate-project.service';
import { CandidateEducation } from '../candidate-education';
import { CandidateEmployment } from '../candidate-employment';
import { CandidateEducationProjectPopupService } from './candidate-education-project-popup.service';
import { CandidateEmploymentProjectPopupService } from './candidate-employment-project-popup.service';
import { JhiDateUtils } from 'ng-jhipster';
import {
    CANDIDATE_EDUCATION_ID,
    CANDIDATE_PROJECT_ID,
    CANDIDATE_EMPLOYMENT_ID,
    IS_EMPLOYMENT_PROJECT
} from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { Router } from '@angular/router';

@Component({
    selector: 'jhi-candidate-project-dialog',
    templateUrl: './candidate-project-dialog.component.html'
})
export class CandidateProjectDialogComponent implements OnInit {
    candidateProject: CandidateProject;
    authorities: any[];
    isSaving: boolean;
    options: Object;
    candidateEducations: CandidateEducation[];
    endDateLesser: boolean;
    endDateControl: boolean;
    isEmploymentProject: boolean;
    candidateEmployments: CandidateEmployment[];
    projectStartDateDp: any;
    projectEndDateDp: any;
    editorConfig: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private candidateProjectService: CandidateProjectService,
        private router: Router,
        private eventManager: JhiEventManager,
        private dateUtils: JhiDateUtils,
        private config: NgbDatepickerConfig,
        private activatedRoute: ActivatedRoute
    ) {}

    configureDatePicker() {
        this.config.minDate = { year: 1980, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };

        // days that don't belong to current month are not visible
        this.config.outsideDays = 'hidden';
    }

    manageEndDateControl() {
        if (this.candidateProject.isCurrentProject) {
            this.endDateControl = true;
            this.candidateProject.projectEndDate = null;
            this.endDateLesser = false;
        } else {
            this.endDateControl = false;
        }
    }
    validateDates() {
        this.endDateLesser = false;
        if (this.candidateProject.projectStartDate && this.candidateProject.projectEndDate) {
            const fromDate = new Date(this.candidateProject.projectStartDate.format(DATE_FORMAT));
            const toDate = new Date(this.candidateProject.projectEndDate.format(DATE_FORMAT));

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
        // this.options = new EditorProperties().options;
        this.endDateLesser = false;
        this.candidateProject.isCurrentProject ? (this.endDateControl = true) : (this.endDateControl = false);
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    clearRoute() {
        if (this.isEmploymentProject) {
            this.router.navigate(['/employment', { outlets: { popup: null } }]);
        } else {
            this.router.navigate(['/education', { outlets: { popup: null } }]);
        }
    }

    save() {
        this.isSaving = true;
        if (this.candidateProject.id !== undefined) {
            this.subscribeToSaveResponse(this.candidateProjectService.update(this.candidateProject));
        } else {
            this.subscribeToSaveResponse(this.candidateProjectService.create(this.candidateProject));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<CandidateProject>>) {
        result.subscribe(
            (res: HttpResponse<CandidateProject>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess(result: CandidateProject) {
        this.eventManager.broadcast({ name: 'candidateProjectListModification', content: 'OK' });
        //  this.eventManager.broadcast({name: 'candidateEducationListModification', content: 'OK'});
        this.eventManager.broadcast({ name: 'candidateEmploymentListModification', content: 'OK' });
        this.isSaving = false;
        this.clearRoute();
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
        this.router.navigate(['/error']);
    }

    trackCandidateEducationById(index: number, item: CandidateEducation) {
        return item.id;
    }

    trackCandidateEmploymentById(index: number, item: CandidateEmployment) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-candidate-project-popup',
    template: ''
})
export class CandidateProjectPopupComponent implements OnInit, OnDestroy {
    isEmploymentProject: boolean;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateProjectPopupService: CandidateProjectPopupService,
        private dataStorageService: DataStorageService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (this.dataStorageService.getData(IS_EMPLOYMENT_PROJECT) === 'true') {
                this.isEmploymentProject = true;
            } else {
                this.isEmploymentProject = false;
            }
            if (params['id']) {
                this.candidateProjectPopupService.open(
                    CandidateProjectDialogComponent as Component,
                    params['id'],
                    this.isEmploymentProject
                );
            } else if (this.dataStorageService.getData(CANDIDATE_PROJECT_ID)) {
                this.candidateProjectPopupService.open(
                    CandidateProjectDialogComponent as Component,
                    this.dataStorageService.getData(CANDIDATE_PROJECT_ID),
                    this.isEmploymentProject
                );
            } else {
                this.candidateProjectPopupService.open(CandidateProjectDialogComponent as Component, this.isEmploymentProject);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
@Component({
    selector: 'jhi-candidate-project-popup',
    template: ''
})
export class CandidateEducationProjectPopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateEducationProjectPopupService: CandidateEducationProjectPopupService,
        private dataStorageService: DataStorageService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.candidateEducationProjectPopupService.open(CandidateProjectDialogComponent as Component, params['id']);
            } else {
                this.candidateEducationProjectPopupService.open(
                    CandidateProjectDialogComponent as Component,
                    this.dataStorageService.getData(CANDIDATE_EDUCATION_ID)
                );
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

@Component({
    selector: 'jhi-candidate-project-popup',
    template: ''
})
export class CandidateEmploymentProjectPopupComponent implements OnInit, OnDestroy {
    routeSub: any;
    isEmployment: boolean;
    constructor(
        private route: ActivatedRoute,
        private candidateEmploymentProjectPopupService: CandidateEmploymentProjectPopupService,
        private dataStorageService: DataStorageService
    ) {}

    ngOnInit() {
        this.isEmployment = true;
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.candidateEmploymentProjectPopupService.open(
                    CandidateProjectDialogComponent as Component,
                    params['id'],
                    this.isEmployment
                );
            } else if (this.dataStorageService.getData(CANDIDATE_EMPLOYMENT_ID)) {
                this.candidateEmploymentProjectPopupService.open(
                    CandidateProjectDialogComponent as Component,
                    this.dataStorageService.getData(CANDIDATE_EMPLOYMENT_ID),
                    this.isEmployment
                );
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
