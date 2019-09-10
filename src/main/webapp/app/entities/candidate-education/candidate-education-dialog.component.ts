import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {NgForm} from "@angular/forms";
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';
import {NgxSpinnerService} from 'ngx-spinner';
import {CandidateEducation} from './candidate-education.model';
import {CandidateEducationPopupService} from './candidate-education-popup.service';
import {CandidateEducationService} from './candidate-education.service';
import {Candidate, CandidateService} from '../candidate';
import {Qualification, QualificationService} from '../qualification';
import {Course, CourseService} from '../course';
import {College, CollegeService} from '../college';
import {University, UniversityService} from '../university';
import {CandidateEducationPopupServiceNew} from './candidate-education-popup-new.service';
import {AuthoritiesConstants} from '../../shared/authorities.constant';
import {Principal,DataStorageService} from '../../shared';
import { CANDIDATE_ID, CANDIDATE_EDUCATION_ID, USER_ID, USER_DATA } from '../../shared/constants/storage.constants';
import { ViewChild } from '@angular/core';
import {JhiDateUtils} from 'ng-jhipster';
//import {EducationCollegeService} from './education-college.service';

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

  //qualifications: Qualification[];

  //courses: Course[];
  showQualificationTextArea: boolean;
  showCollegeTextArea: boolean;
  showCourseTextArea: boolean;

  //  colleges: College[];
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
  currentDate : Date;
  searching = false;
  searchFailed = false;
  hideSearchingWhenUnsubscribed = new Observable(() => () => this.searching = false);


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
   // private educationCollegeService: EducationCollegeService,
    private activatedRoute: ActivatedRoute,
    private principal: Principal,
    private dateUtils: JhiDateUtils,
    private dataService: DataStorageService,
    private spinnerService: NgxSpinnerService
    

  ) {
    this.currentDate = new Date();
  }

  requestCollegeData = (text: string): Observable<HttpResponse<any>> => {
    return this.collegeService.searchRemote({
      query: text
    }).map((data) => data.body === '' ? '' : data.body);
  }

  requestQualificationData = (text: string): Observable<HttpResponse<any>> => {
    return this.qualificationService.searchRemote({
      query: text
    }).map((data) => data.body === '' ? '' : data.body);;
  }

  requestCourseData = (text: string): Observable<HttpResponse<any>> => {
    return this.courseService.searchRemote({
      query: text
    }).map((data) => data.body === '' ? '' : data.body);
  }

  requestUniversityData = (text: string): Observable<HttpResponse<any>> => {
    return this.universityService.searchRemote({
      query: text
    }).map((data) => data.body === '' ? '' : data.body);
  }


  isQualificationOther(event) {
    this.showQualificationTextArea = false;
    if (this.candidateEducation.qualification) {
      const qualification = this.candidateEducation.qualification as any;
      if (qualification[0].value === 'Other') {
        this.showQualificationTextArea = true;
      }

    }
  }

  isCourseOther(event) {
    this.showCourseTextArea = false;
    if (this.candidateEducation.course) {
      const course = this.candidateEducation.course as any;
      if (course[0].value === 'Other') {
        this.showCourseTextArea = true;
      }

    }
  }

  isCollegeOther() {
    // console.log('Event is ' + this.candidateEducation.college);
    // this.convertToCollege();
    this.showCollegeTextArea = false;
    if (this.candidateEducation.college) {
      const college = this.candidateEducation.college as any;
      if (college[0].value === 'Other') {
        this.showCollegeTextArea = true;
      }
    }
  }



  setScoreControl() {
    // console.log("the control value is " + JSON.stringify(this.candidateEducation.scoreType));
    this.enableGpa = false;
    this.enablePercent = false;
    if (this.candidateEducation.scoreType === 'gpa') {
      this.enableGpa = true;
    }
    else {
      this.enablePercent = true;
    }

  }

  isPercentValid() {
    this.validPercentScore = true;
    // console.log("Percent =" + JSON.stringify(this.candidateEducation.percentage));
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
     this.jhiAlertService.addAlert({type: 'info', msg: 'gradzcircleApp.candidateEducation.home.highestQualificationAlert', timeout: 5000}, []);
  }
  
  ngOnInit() {
    // this.showCollegeTextArea = false;
    this.validPercentScore = true;
    this.setAlert();
    //  this.showQualificationTextArea = false;
    // this.showCourseTextArea = false;
    this.isSaving = false;
    if (this.candidateEducation.scoreType === 'percent' || this.candidateEducation.scoreType == null) {
      this.enablePercent = true;
      this.candidateEducation.scoreType = 'percent'
    } else {
      this.enableGpa = true;
    }
    // console.log("what is on init"+JSON.stringify(this.enablePercent));
    this.candidateEducation.isPursuingEducation ? this.endDateControl = true : this.endDateControl = false;
    if (this.candidateEducation.college) {
      this.candidateEducation.college.length === 0 ? this.showCollegeTextArea = true : this.showCollegeTextArea = false;
    }
    if (this.candidateEducation.course) {
      this.candidateEducation.course.length === 0 ? this.showCourseTextArea = true : this.showCourseTextArea = false;
    }
    if (this.candidateEducation.qualification) {
      this.candidateEducation.qualification.length === 0 ? this.showQualificationTextArea = true : this.showQualificationTextArea = false;
    }
    this.enableDecimal = true;
    this.gpaValues = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
    this.gpaDecimalValues = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9];
    this.endDateLesser = false;
    this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];

    if (this.principal.hasAnyAuthorityDirect([AuthoritiesConstants.ADMIN])) {
      this.candidateService.query().subscribe(
       (res: HttpResponse<Candidate[]>) => this.candidates = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message));
      /* this.qualificationService.query().subscribe(
         (res: Response) => {this.qualifications = res.json();}, (res: Response) => this.onError(res.json()));
       this.courseService.query().subscribe(
         (res: Response) => {this.courses = res.json();}, (res: Response) => this.onError(res.json()));
        this.collegeService.query().subscribe(
          (res: Response) => {this.colleges = res.json();}, (res: Response) => this.onError(res.json()));*/
    } else if (this.principal.hasAnyAuthorityDirect([AuthoritiesConstants.CANDIDATE])) {
      // console.log("Courses preloaded " + JSON.stringify(this.courses));
      // console.log("Colleges preloaded " + JSON.stringify(this.colleges));
      // console.log("qualifcation preloaded " + JSON.stringify(this.qualifications));
    }
  }



  clear() {
    this.jhiAlertService.clear();
    this.activeModal.dismiss('cancel');
  }
  /* searchQualification = (text$: Observable<string>) =>
     text$
       .debounceTime(100)
       .distinctUntilChanged()
       .map(term => term.length < 2 ? []
         : this.qualifications.filter
           (v => v.qualification ? v.qualification.toLowerCase().indexOf(term.toLowerCase()) > -1 : '').slice(0, 10));
 
   qualificationFormatter = (n: {qualification: string}) => n.qualification;
 
   searchCourse = (text$: Observable<string>) =>
     text$
       .debounceTime(200)
       .distinctUntilChanged()
       .map(term => term.length < 2 ? []
         : this.courses.filter(v => v.course ?
           v.course.toLowerCase().indexOf(term.toLowerCase()) > -1 : '').slice(0, 10));
   courseFormatter = (c: {course: string}) => c.course;
 
   searchCollege = (text$: Observable<string>) =>
     text$
       .debounceTime(200)
       .map(term => term.length < 2 ? []
         : this.colleges.filter(v => v.collegeName ?
           v.collegeName.toLowerCase().indexOf(term.toLowerCase()) > -1 : '').slice(0, 10));
   collegeFormatter = (x: {collegeName: string}) => x.collegeName;
 */

  // searchCollege = (text$: Observable<string>) =>
  //     text$
  //     .debounceTime(300)
  //     .distinctUntilChanged()
  //     .do(() => this.searching = true)
  //     .switchMap(term =>
  //       this.educationCollegeService.search(term)
  //         .do(() => this.searchFailed = false)
  //         .catch(() => {
  //           this.searchFailed = true;
  //           return Observable.of([]);
  //         }))
  //     .do(() => this.searching = false)
  //     .merge(this.hideSearchingWhenUnsubscribed);
  // //collegeFormatter = (x: { collegeName: string }) => x.collegeName;

  save() {
    this.isSaving = true;
    this.validateAndResetScoreBeforeSave();
    this.spinnerService.show();
    if (this.candidateEducation.id !== undefined) {
      this.subscribeToSaveResponse(
        this.candidateEducationService.update(this.candidateEducation));
    } else {
      this.subscribeToSaveResponse(
        this.candidateEducationService.create(this.candidateEducation));
    }
  }

  validateAndResetScoreBeforeSave() {
    if (this.candidateEducation.scoreType === 'gpa') {
      this.candidateEducation.percentage = null;
    }  else {
      this.candidateEducation.gradeDecimal = null;
      this.candidateEducation.roundOfGrade = null;
      this.candidateEducation.grade = null;
    }

  }

  manageEndDateControl() {
    if (this.candidateEducation.isPursuingEducation) {
      this.endDateControl = true;
      this.candidateEducation.educationToDate = '';
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
      const startDate = new Date(this.dateUtils
        .convertLocalDateToServer(this.candidateEducation.educationFromDate));
      const endDate = new Date(this.dateUtils
        .convertLocalDateToServer(this.candidateEducation.educationToDate));
      if (startDate > endDate) {
        this.endDateLesser = true;
    //   this.currentForm.form.setErrors({ 'valid': false });
      } else {
        this.endDateLesser = false;
        //this.currentForm.form.setErrors(null);
      }
    } 


  }

   private subscribeToSaveResponse(result: Observable<HttpResponse<CandidateEducation>>) {
        result.subscribe((res: HttpResponse<CandidateEducation>) => this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError(res));
    }

  private onSaveSuccess(result: CandidateEducation) {
    //console.log('on suucess');
    this.eventManager.broadcast({name: 'candidateEducationListModification', content: 'OK'});
     this.eventManager.broadcast({name: 'candidateListModification', content: 'OK'});
    this.reloadCandidate();
    this.isSaving = false;
    this.spinnerService.hide();
    this.activeModal.dismiss(result);
  }

   reloadCandidate() {
   // console.log('Reloading candidate??');
    this.candidateService.getCandidateDetails(this.dataService.getData(USER_ID)).subscribe(
      (res: HttpResponse<Candidate>) => {
        this.dataService.setdata(USER_DATA, JSON.stringify(res.body)); 
  },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    return;
  }
  
  private onSaveError(result:any) {
   this.spinnerService.hide();
    this.isSaving = false;
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

    this.routeSub = this.route.params.subscribe((params) => {
      if (params['id']) {
        this.candidateEducationPopupService
          .open(CandidateEducationDialogComponent as Component, params['id']);
      } else {
       const id = this.dataService.getData(CANDIDATE_EDUCATION_ID);
        if (id) {
          this.candidateEducationPopupService
          .open(CandidateEducationDialogComponent as Component, id);
        } else {
        this.candidateEducationPopupService
          .open(CandidateEducationDialogComponent as Component);
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
export class CandidateEducationPopupComponentNew implements OnInit, OnDestroy {


  routeSub: any;

  constructor(
    private route: ActivatedRoute,
    private candidateEducationPopupService: CandidateEducationPopupServiceNew,
    private dataService: DataStorageService
  ) {}

  ngOnInit() {

    this.routeSub = this.route.params.subscribe((params) => {
      if (params['id']) {
      this.candidateEducationPopupService
        .open(CandidateEducationDialogComponent as Component, params['id']);
      } else { 
        const id = this.dataService.getData(CANDIDATE_ID);
        if (id) {
           this.candidateEducationPopupService
        .open(CandidateEducationDialogComponent as Component, id);
        }
      }

    });
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
  }
}
