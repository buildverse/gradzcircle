import { Principal } from '../../core/auth/principal.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef, NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { NgxSpinnerService } from 'ngx-spinner';
import { CandidateEducation } from './candidate-education.model';
import { CandidateEducationPopupService } from './candidate-education-popup.service';
import { CandidateEducationService } from './candidate-education.service';
import { Candidate, CandidateService } from '../candidate';
import { Qualification, QualificationService } from '../qualification';
import { Course, CourseService } from '../course';
import { College, CollegeService } from '../college';
import { University, UniversityService } from '../university';
import { CandidateEducationPopupServiceNew } from './candidate-education-popup-new.service';
import { AuthoritiesConstants } from '../../shared/authorities.constant';
import { CANDIDATE_ID, CANDIDATE_EDUCATION_ID, USER_ID, USER_DATA, HAS_EDUCATION } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { ViewChild } from '@angular/core';
import { DATE_FORMAT } from '../../shared/constants/input.constants';

@Component({
    selector: 'jhi-candidate-education-dialog',
    templateUrl: './candidate-education-dialog.component.html'
})
export class CandidateEducationDialogComponent implements OnInit {
    @ViewChild('editForm') currentForm: NgForm;

    candidateEducation: CandidateEducation;
    authorities: any[];
    isSaving: boolean;
    candidateId: any;

    candidates: Candidate[];

    qualifications: Qualification[];

    courses: Course[];
    showQualificationTextArea: boolean;
    showCollegeTextArea: boolean;
    showCourseTextArea: boolean;

    colleges: College[];
    educationFromDateDp: any;
    educationToDateDp: any;
    scoreType: string;
    enableGpa: boolean;
    enablePercent: boolean;
    gpaValues: number[];
    gpaDecimalValues: number[];
    validPercentScore: boolean;
    enableDecimal: boolean;
    endDateControl: boolean;
    endDateLesser: boolean;
    endDateInFuture: boolean;
    currentDate: Date;
    searching = false;
    searchFailed = false;
    hideSearchingWhenUnsubscribed = new Observable(() => () => (this.searching = false));
    fromProfile: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private candidateEducationService: CandidateEducationService,
        private candidateService: CandidateService,
        private qualificationService: QualificationService,
        private courseService: CourseService,
        private collegeService: CollegeService,
        private universityService: UniversityService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private dataService: DataStorageService,
        private spinnerService: NgxSpinnerService,
        private config: NgbDatepickerConfig,
        private router: Router
    ) {
        this.currentDate = new Date();
    }

    configureDatePicker() {
        this.config.minDate = { year: 1980, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };

        // days that don't belong to current month are not visible
        this.config.outsideDays = 'hidden';
    }

    requestCollegeData = (text: string): Observable<HttpResponse<any>> => {
        return this.collegeService
            .searchRemote({
                query: text
            })
            .map(data => (data.body === '' ? '' : data.body));
    };

    requestQualificationData = (text: string): Observable<HttpResponse<any>> => {
        return this.qualificationService
            .searchRemote({
                query: text
            })
            .map(data => (data.body === '' ? '' : data.body));
    };

    requestCourseData = (text: string): Observable<HttpResponse<any>> => {
        return this.courseService
            .searchRemote({
                query: text
            })
            .map(data => (data.body === '' ? '' : data.body));
    };

    requestUniversityData = (text: string): Observable<HttpResponse<any>> => {
        return this.universityService
            .searchRemote({
                query: text
            })
            .map(data => (data.body === '' ? '' : data.body));
    };

    isQualificationOther() {
        this.showQualificationTextArea = false;
        if (this.candidateEducation.qualification) {
            const qualification = this.candidateEducation.qualification as any;
            if (qualification[0].value === 'Other') {
                this.showQualificationTextArea = true;
            }
        }
    }

    isCourseOther() {
        this.showCourseTextArea = false;
        if (this.candidateEducation.course) {
            const course = this.candidateEducation.course as any;
            if (course[0].value === 'Other') {
                this.showCourseTextArea = true;
            }
        }
    }

    isCollegeOther() {
        this.showCollegeTextArea = false;
        if (this.candidateEducation.college) {
            const college = this.candidateEducation.college as any;
            if (college[0].value === 'Other') {
                this.showCollegeTextArea = true;
            }
        }
    }

    setScoreControl() {
        this.enableGpa = false;
        this.enablePercent = false;
        if (this.candidateEducation.scoreType === 'gpa') {
            this.enableGpa = true;
        } else {
            this.enablePercent = true;
        }
    }

    isPercentValid() {
        this.validPercentScore = true;
        if (this.candidateEducation.percentage) {
            if (this.candidateEducation.percentage > 100) {
                this.validPercentScore = false;
            }
        }
    }

    shouldDisableDecimal() {
        this.enableDecimal = true;
        if (this.candidateEducation.roundOfGrade) {
            if (this.candidateEducation.roundOfGrade === 10) {
                this.candidateEducation.gradeDecimal = 0;
                this.enableDecimal = false;
            }
        }
    }

    setAlert() {
        this.jhiAlertService.addAlert(
            { type: 'info', msg: 'gradzcircleApp.candidateEducation.home.highestQualificationAlert', timeout: 5000 },
            []
        );
    }

    ngOnInit() {
        this.configureDatePicker();
        this.validPercentScore = true;
        this.setAlert();
        this.isSaving = false;
        if (this.candidateEducation.scoreType === 'percent' || this.candidateEducation.scoreType == null) {
            this.enablePercent = true;
            this.candidateEducation.scoreType = 'percent';
        } else {
            this.enableGpa = true;
        }
        this.candidateEducation.isPursuingEducation ? (this.endDateControl = true) : (this.endDateControl = false);
        if (this.candidateEducation.college) {
            this.candidateEducation.college.length === 0 ? (this.showCollegeTextArea = true) : (this.showCollegeTextArea = false);
        }
        if (this.candidateEducation.course) {
            this.candidateEducation.course.length === 0 ? (this.showCourseTextArea = true) : (this.showCourseTextArea = false);
        }
        if (this.candidateEducation.qualification) {
            this.candidateEducation.qualification.length === 0
                ? (this.showQualificationTextArea = true)
                : (this.showQualificationTextArea = false);
        }
        this.enableDecimal = true;
        this.gpaValues = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
        this.gpaDecimalValues = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9];
        this.endDateLesser = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];

        if (this.principal.hasAnyAuthorityDirect([AuthoritiesConstants.ADMIN])) {
            this.candidateService
                .query()
                .subscribe(
                    (res: HttpResponse<Candidate[]>) => (this.candidates = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        } else if (this.principal.hasAnyAuthorityDirect([AuthoritiesConstants.CANDIDATE])) {
        }
    }

    clear() {
        this.jhiAlertService.clear();
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    clearRoute() {
        if (this.fromProfile) {
            this.router.navigate(['/candidate-profile', { outlets: { popup: null } }]);
        } else {
            this.router.navigate(['/education', { outlets: { popup: null } }]);
        }
    }

    save() {
        this.isSaving = true;
        this.validateAndResetScoreBeforeSave();
        this.spinnerService.show();
        if (this.candidateEducation.id !== undefined) {
            this.subscribeToSaveResponse(this.candidateEducationService.update(this.candidateEducation));
        } else {
            this.subscribeToSaveResponse(this.candidateEducationService.create(this.candidateEducation));
        }
    }

    validateAndResetScoreBeforeSave() {
        if (this.candidateEducation.scoreType === 'gpa') {
            this.candidateEducation.percentage = null;
        } else {
            this.candidateEducation.gradeDecimal = null;
            this.candidateEducation.roundOfGrade = null;
            this.candidateEducation.grade = null;
        }
    }

    manageEndDateControl() {
        if (this.candidateEducation.isPursuingEducation) {
            this.endDateControl = true;
            this.candidateEducation.educationToDate = null;
            this.endDateLesser = false;
        } else {
            this.endDateControl = false;
        }
    }

    validateDates() {
        if (this.candidateEducation.isPursuingEducation) {
            this.manageEndDateControl();
            return;
        }
        this.endDateLesser = false;
        this.endDateInFuture = false;
        if (this.candidateEducation.educationFromDate && this.candidateEducation.educationToDate) {
            const startDate = new Date(this.candidateEducation.educationFromDate.format(DATE_FORMAT));
            const endDate = new Date(this.candidateEducation.educationToDate.format(DATE_FORMAT));
            if (startDate > endDate) {
                this.endDateLesser = true;
            } else {
                this.endDateLesser = false;
            }
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<CandidateEducation>>) {
        result.subscribe(
            (res: HttpResponse<CandidateEducation>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => this.onSaveError(res)
        );
    }

    private onSaveSuccess(result: CandidateEducation) {
        this.eventManager.broadcast({ name: 'candidateEducationListModification', content: 'OK' });
        this.eventManager.broadcast({ name: 'candidateListModification', content: 'OK' });
        this.reloadCandidate();
        this.isSaving = false;
        this.spinnerService.hide();
        this.clearRoute();
        this.activeModal.dismiss(result);
    }

    reloadCandidate() {
        this.candidateService.getCandidateDetails(this.dataService.getData(USER_ID)).subscribe(
            (res: HttpResponse<Candidate>) => {
                this.dataService.setdata(USER_DATA, JSON.stringify(res.body));
                this.dataService.setdata(HAS_EDUCATION, res.body.hasEducation);
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        return;
    }

    private onSaveError(result: any) {
        this.spinnerService.hide();
        this.isSaving = false;
        this.clearRoute();
        this.router.navigate(['/error']);
        this.activeModal.dismiss();
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCandidateById(index: number, item: Candidate) {
        return item.id;
    }

    trackQualificationById(index: number, item: Qualification) {
        return item.id;
    }

    trackCourseById(index: number, item: Course) {
        return item.id;
    }

    trackCollegeById(index: number, item: College) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-candidate-education-popup',
    template: ''
})
export class CandidateEducationPopupComponent implements OnInit, OnDestroy {
    routeSub: any;
    courses: Course[];
    qualifications: Qualification[];
    colleges: College[];

    constructor(
        private route: ActivatedRoute,
        private candidateEducationPopupService: CandidateEducationPopupService,
        private dataService: DataStorageService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.candidateEducationPopupService.open(CandidateEducationDialogComponent as Component, params['id']);
            } else {
                const id = this.dataService.getData(CANDIDATE_EDUCATION_ID);
                if (id) {
                    this.candidateEducationPopupService.open(CandidateEducationDialogComponent as Component, id);
                } else {
                    this.candidateEducationPopupService.open(CandidateEducationDialogComponent as Component);
                }
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
@Component({
    selector: 'jhi-candidate-education-popup',
    template: ''
})
export class CandidateEducationPopupNewComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateEducationPopupService: CandidateEducationPopupServiceNew,
        private dataService: DataStorageService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.candidateEducationPopupService.open(
                    CandidateEducationDialogComponent as Component,
                    params['id'],
                    params['fromProfile']
                );
            } else {
                const id = this.dataService.getData(CANDIDATE_ID);
                if (id) {
                    this.candidateEducationPopupService.open(CandidateEducationDialogComponent as Component, id, params['fromProfile']);
                }
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
