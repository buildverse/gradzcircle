import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CandidateProject } from './candidate-project.model';
import { CandidateProjectPopupService } from './candidate-project-popup.service';
import { CandidateProjectService } from './candidate-project.service';
import { CandidateEducation, CandidateEducationService } from '../candidate-education';
import { CandidateEmployment, CandidateEmploymentService } from '../candidate-employment';
import { CandidateEducationProjectPopupService } from './candidate-education-project-popup.service';
import { CandidateEmploymentProjectPopupService } from './candidate-employment-project-popup.service';
import { JhiDateUtils } from 'ng-jhipster';
import { ResponseWrapper,EditorProperties } from '../../shared';

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
    endDateControl:boolean;
    isEmploymentProject: boolean;
    candidateEmployments: CandidateEmployment[];
    projectStartDateDp: any;
    projectEndDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private candidateProjectService: CandidateProjectService,
        private candidateEducationService: CandidateEducationService,
        private candidateEmploymentService: CandidateEmploymentService,
        private eventManager: JhiEventManager,
        private dateUtils: JhiDateUtils
    ) {
    }

 manageEndDateControl() {
        if (this.candidateProject.isCurrentProject) {
            this.endDateControl = true;
            this.candidateProject.projectEndDate='';
            this.endDateLesser =false;
        } else {
            this.endDateControl = false;

        }
    }
    validateDates() {
         this.endDateLesser = false;
        if(this.candidateProject.projectStartDate && this.candidateProject.projectEndDate){
        let fromDate = new Date (this.dateUtils
             .convertLocalDateToServer(this.candidateProject.projectStartDate));
        let toDate = new Date(this.dateUtils
             .convertLocalDateToServer(this.candidateProject.projectEndDate));

        if(fromDate > toDate )
            this.endDateLesser = true;
        else
            this.endDateLesser = false;
        }


    }

    ngOnInit() {
        this.isSaving = false;
        this.options = new EditorProperties().options;
        this.endDateLesser = false;
        this.candidateProject.isCurrentProject?this.endDateControl=true:this.endDateControl=false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.candidateEducationService.query()
            .subscribe((res: ResponseWrapper) => { this.candidateEducations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.candidateEmploymentService.query()
            .subscribe((res: ResponseWrapper) => { this.candidateEmployments = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.candidateProject.id !== undefined) {
            this.subscribeToSaveResponse(
                this.candidateProjectService.update(this.candidateProject));
        } else {
            this.subscribeToSaveResponse(
                this.candidateProjectService.create(this.candidateProject));
        }
    }

    private subscribeToSaveResponse(result: Observable<CandidateProject>) {
        result.subscribe((res: CandidateProject) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: CandidateProject) {
         this.eventManager.broadcast({ name: 'candidateProjectListModification', content: 'OK' });
        this.eventManager.broadcast({ name: 'candidateEducationListModification', content: 'OK' });
        this.eventManager.broadcast({ name: 'candidateEmploymentListModification', content: 'OK' });
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
        private candidateProjectPopupService: CandidateProjectPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if(params['isEmploymentProject']==='true'){
                this.isEmploymentProject = true;
            }

            if (params['id']) {
                this.candidateProjectPopupService
                    .open(CandidateProjectDialogComponent as Component, params['id'],this.isEmploymentProject);
            } else {
                this.candidateProjectPopupService
                    .open(CandidateProjectDialogComponent as Component,this.isEmploymentProject);
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
        private candidateEducationProjectPopupService: CandidateEducationProjectPopupService
    ) { }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                 this.candidateEducationProjectPopupService
                    .open(CandidateProjectDialogComponent as Component, params['id']);

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
        private candidateEmploymentProjectPopupService: CandidateEmploymentProjectPopupService

    ) { }

    ngOnInit() {
        this.isEmployment = true;
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                 this.candidateEmploymentProjectPopupService
                    .open(CandidateProjectDialogComponent as Component, params['id'],this.isEmployment);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
