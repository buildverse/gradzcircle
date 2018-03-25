import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Response} from '@angular/http';
import {of} from 'rxjs/observable/of';
import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';
import {Job, PaymentType} from './job.model';
import {JobPopupService} from './job-popup.service';
import {JobPopupServiceNew} from './job-popup-new.service';
import {JobService} from './job.service';
import {JobConstants} from './job.constants';
import {JobType, JobTypeService} from '../job-type';
import {EmploymentType, EmploymentTypeService} from '../employment-type';
import {Corporate, CorporateService} from '../corporate';
import {Candidate, CandidateService} from '../candidate';
import {ResponseWrapper} from '../../shared';
import {Principal} from '../../shared/auth/principal.service';
import {AuthoritiesConstants} from '../../shared/authorities.constant';
import {Qualification, QualificationService} from '../qualification';
import {Course, CourseService} from '../course';
import {College, CollegeService} from '../college';
import {Gender, GenderService} from '../gender';
import {FilterCategory, FilterCategoryService} from '../filter-category';
import {University, UniversityService} from '../university/index';
import {Language, LanguageService} from '../language';
import {Filter, FilterService} from '../filter';
import {JobCategoryDeleteDialogComponent} from '../job-category';
import {JhiDateUtils} from 'ng-jhipster';
import {JobFilterService} from '../job-filter/job-filter.service';
import {JobFilter} from '../job-filter/index';
import * as equal from 'fast-deep-equal';
import {AppConfig} from '../app-config/app-config.model';
import {AppConfigService} from '../app-config/app-config.service';


function deepcopy<T>(o: T): T {
  return JSON.parse(JSON.stringify(o));
}


@Component({
  selector: 'jhi-job-dialog',
  templateUrl: './job-dialog.component.html'
})
export class JobDialogComponent implements OnInit {

  job: Job;
  prevJob: Job;
  isSaving: boolean;
  jobtypes: JobType[];
  numberOfCandidateError: boolean;
  employmentTypes: EmploymentType[];
  qualifications: Qualification[];
  courses: Course[];
  universities: University[];
  corporates: Corporate[];
  candidates: Candidate[];
  languages: Language[];
  colleges: College[];
  genders: Gender[];
  appConfigs: AppConfig[];
  gender: Gender;
  jobPostDateDp: any;
  filterCost: number;
  graduationDateAdded; graduationToDateAdded; graduationFromDateAdded; isNew; businessPlanEnabled: boolean;
  scoreType; graduationDateType: string;
  graduationDate; graduationToDate; graduationFromDate: any;
  gradDate; gradToDate; gradFromDate: Date;
  gpaValues: number[];
  gpaDecimalValues: number[];
  percentage; qualificationCost; coursesCost; scoreCost; genderCost; languageCost; gradDateCost; jobCost; discountedJobCost;escrowAmount: number;
  gpa: number;
  roundOfGrade; gradeDecimal: number;
  filters: Filter[];
  enableGpa; enablePercent; validPercentScore; enableDecimal; useEscrow: boolean;
  multipleDateControl; singleDateControl; endDateLesser: boolean;
  filterMap: Map<string, number>;
  QUALIFICATION; LANGUAGE; SCORE; GRADUATION_DATE; COLLEGE; UNIVERSITY; COURSE; GENDER; DISCOUNT; AMEND: string;
  jobTypeCost: number[];
  employmentTypeCost: number[];
  scoreAdded; genderAdded; preview: boolean;
  premium; addOn; basic: boolean;
  DRAFT = JobConstants.DRAFT;
  ACTIVE = JobConstants.ACTIVE;
  jobFilter: JobFilter;
  prevJobCost: number;
  jobCostDifference: number;
  paymentType: string;
  amountOutstanding: number;
  account: any;
  constructor(
    public activeModal: NgbActiveModal,
    private jhiAlertService: JhiAlertService,
    private jobService: JobService,
    private jobTypeService: JobTypeService,
    private employmentTypeService: EmploymentTypeService,
    private corporateService: CorporateService,
    private candidateService: CandidateService,
    private eventManager: JhiEventManager,
    private qualificationService: QualificationService,
    private courseService: CourseService,
    private collegeService: CollegeService,
    private universityService: UniversityService,
    private languageService: LanguageService,
    private filterService: FilterService,
    private genderService: GenderService,
    private dateUtils: JhiDateUtils,
    private jobFilterService: JobFilterService,
    private appConfigService: AppConfigService,
    private router: Router,
    private principal: Principal

  ) {
  }

  ngOnInit() {
    this.discountedJobCost = 0;
    this.graduationDateAdded = false;
    this.graduationFromDateAdded = false;
    this.graduationToDateAdded = false;
    this.businessPlanEnabled = false;
    this.isSaving = false;
    this.qualificationCost = 0;
    this.coursesCost = 0;
    this.scoreCost = 0;
    this.gradDateCost = 0;
    this.languageCost = 0;
    this.genderCost = 0;
    this.jobCostDifference = 0;
    this.escrowAmount =0;
    this.scoreAdded = false;
    this.genderAdded = false;
    this.filterMap = new Map<string, number>();
    this.jobTypeCost = [];
    this.employmentTypeCost = [];
    this.jobFilter = new JobFilter();
    this.basic = true;
    this.jobTypeService.query()
      .subscribe((res: ResponseWrapper) => {this.jobtypes = res.json;}, (res: ResponseWrapper) => this.onError(res.json));
    this.employmentTypeService.query()
      .subscribe((res: ResponseWrapper) => {this.employmentTypes = res.json;}, (res: ResponseWrapper) => this.onError(res.json));
    this.appConfigService.query()
      .subscribe((res: ResponseWrapper) => {
        this.appConfigs = res.json;
        this.appConfigs.forEach((appConfig) => {
          if (appConfig.configName.toUpperCase().indexOf('BusinessPlan'.toUpperCase()) > -1) {
            this.businessPlanEnabled = appConfig.configValue;
          }
        });
      }
      , (res: ResponseWrapper) => this.onError(res.json));
    this.filterService.query()
      .subscribe((res: ResponseWrapper) => {
        this.filters = res.json;
        console.log('filter data is ' + JSON.stringify(this.filters));
        this.filters.forEach((filter) => {
          this.filterMap.set(filter.filterName, filter.filterCost);
          if (filter.filterName.indexOf('grad') > -1) {
            this.GRADUATION_DATE = filter.filterName;
          } else if (filter.filterName.toUpperCase().indexOf('score'.toUpperCase()) > -1) {
            this.SCORE = filter.filterName;
          } else if (filter.filterName.toUpperCase().indexOf('coll'.toUpperCase()) > -1) {
            this.COLLEGE = filter.filterName;
          } else if (filter.filterName.toUpperCase().indexOf('uni'.toUpperCase()) > -1) {
            this.UNIVERSITY = filter.filterName;
          } else if (filter.filterName.toUpperCase().indexOf('lang'.toUpperCase()) > -1) {
            this.LANGUAGE = filter.filterName;
          } else if (filter.filterName.toUpperCase().indexOf('gender'.toUpperCase()) > -1) {
            this.GENDER = filter.filterName;
          } else if (filter.filterName.toUpperCase().indexOf('course'.toUpperCase()) > -1) {
            this.COURSE = filter.filterName;
          } else if (filter.filterName.toUpperCase().indexOf('qual'.toUpperCase()) > -1) {
            this.QUALIFICATION = filter.filterName;
          } else if (filter.filterName.toUpperCase().indexOf('disc'.toUpperCase()) > -1) {
            this.DISCOUNT = filter.filterName;
          } else if (filter.filterName.toUpperCase().indexOf('amend'.toUpperCase()) > -1) {
            this.AMEND = filter.filterName;
          }
        });
      }, (res: ResponseWrapper) => this.onError(res.json));
    this.genderService.query()
      .subscribe((res: ResponseWrapper) => {this.genders = res.json;}, (res: ResponseWrapper) => this.onError(res.json));
    this.gpaValues = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
    this.gpaDecimalValues = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9];
    this.useEscrow = false;
    this.filterCost = 0;
    if (this.job.id) {
      this.isNew = false;
      this.prevJob = deepcopy(this.job);

      this.prevJobCost = this.job.jobCost;
      if (this.job.jobFilters) {
        this.initializeFormFilterData();
      }
      this.escrowAmount = this.job.corporate.escrowAmount;
      this.job.jobCost = this.prevJobCost;
      this.amountOutstanding = this.prevJobCost - this.job.amountPaid;
      this.filterCost = this.job.jobCost / this.job.noOfApplicants;
      console.log('check this' + this.filterCost);
    } else {
      this.isNew = true;
      this.job.noOfApplicants = 20;
    }
    this.numberOfCandidateError = false;
    this.principal.identity().then((account) => this.account = account);
  }

  initializeFormFilterData() {
    this.employmentTypeCost.push(this.job.employmentType.employmentTypeCost);
    this.jobTypeCost.push(this.job.jobType.jobTypeCost);
    this.job.jobFilters.forEach((jobFilter) => {
      this.jobFilter.id = jobFilter.id;
      this.basic = jobFilter.filterDescription.basic;
      this.colleges = jobFilter.filterDescription.colleges;
      this.universities = jobFilter.filterDescription.universities;
      this.premium = jobFilter.filterDescription.premium;
      this.courses = jobFilter.filterDescription.courses;
      this.qualifications = jobFilter.filterDescription.qualifications;
      this.scoreType = jobFilter.filterDescription.scoreType;
      this.setScoreControl();
      this.percentage = jobFilter.filterDescription.percentage;
      const scoreSplit = jobFilter.filterDescription.gpa ? jobFilter.filterDescription.gpa.split('.') : [];
      console.log('score split is ' + JSON.stringify(scoreSplit));
      if (scoreSplit.length > 0) {
        this.roundOfGrade = scoreSplit[0];
        this.gradeDecimal = scoreSplit[1];
        console.log('values are ' + this.roundOfGrade + '.' + this.gradeDecimal);
      }
      this.shouldDisableDecimal();
      this.addOn = jobFilter.filterDescription.addOn;
      this.graduationDateType = jobFilter.filterDescription.graduationDateType;
      this.graduationDate = jobFilter.filterDescription.graduationDate;
      this.graduationFromDate = jobFilter.filterDescription.graduationFromDate;
      this.graduationToDate = jobFilter.filterDescription.graduationToDate;
      this.showDateControl();
      this.languages = jobFilter.filterDescription.languages;
      this.gender = jobFilter.filterDescription.gender;
      this.jobCostDifference = 0;

    });

  }

  setScoreControl() {
    this.enableGpa = false;
    this.enablePercent = false;
    if (this.scoreType === 'gpa') {
      this.enableGpa = true;
      this.percentage = undefined;
    } else if (this.scoreType === 'percent') {
      this.enablePercent = true;
      this.roundOfGrade = undefined;
      this.gradeDecimal = undefined;
    }

    this.updateScoreCost();
  }

  setPayment() {
    if (this.paymentType) {
      if (this.paymentType === 'now') {
        this.job.paymentType = PaymentType.UPFRONT;
      } else {
        this.job.paymentType = PaymentType.AS_YOU_GO;
      }
    }
  }

  clearScore() {
    this.roundOfGrade = undefined;
    this.gradeDecimal = undefined;
    this.percentage = undefined;
    this.enableGpa = false;
    this.enablePercent = false;
    this.scoreType = undefined;
    this.updateScoreCost();
  }

  isPercentValid() {
    this.validPercentScore = true;
    if (this.percentage) {
      if (this.percentage > 100) {
        this.validPercentScore = false;
      }
    }
  }

  updateScoreCost() {
    if ((this.validPercentScore && this.percentage) || (this.roundOfGrade)) {
      if (!this.scoreAdded) {
        this.scoreAdded = true;
        this.scoreCost += this.filterMap.get(this.SCORE);
        this.filterCost += this.filterMap.get(this.SCORE);
        this.updateJobCost();
      }
    } else {
      if (this.scoreAdded) {
        this.scoreAdded = false;
        this.scoreCost -= this.filterMap.get(this.SCORE);
        this.filterCost -= this.filterMap.get(this.SCORE);
        this.updateJobCost();
      }
    }
  }

  reEvalCost() {

  }

  shouldDisableDecimal() {
    this.enableDecimal = true;
    if (this.roundOfGrade) {
      if (this.roundOfGrade == 10) {
        this.gradeDecimal = undefined;
        this.enableDecimal = false;
      }
    }

  }
  numOfCandidateValueChange(value) {
    if (value < 20) {
      this.numberOfCandidateError = true;
    } else {
      this.job.noOfApplicants = value;
      this.numberOfCandidateError = false;
      this.updateJobCost();
    }
  }
  togglePremiumFilter() {
    if (!this.premium) {
      if (this.qualifications && this.qualifications.length > 0) {
        this.qualifications.length = 0;
        this.removeQualificationCost();
      }
      if (this.courses && this.courses.length > 0) {
        this.courses.length = 0;
        this.removeCourseCost();
      }
      this.clearScore();
    }
    console.log('Filter cost premium is ' + this.filterCost);
  }

  toggleAddOnFilter() {
    if (!this.addOn) {
      this.clearDates();
      if (this.languages && this.languages.length > 0) {
        this.languages.length = 0;
        this.removeLanguageCost();
      }

      this.gender = undefined;
      // this.genderAdded = false;
      this.updateGenderCost();
    }
    console.log('Filter cost add on is ' + this.filterCost);
  }

  addQualificationCost() {
    if (this.qualifications && this.qualifications.length <= 1) {
      this.qualificationCost += this.filterMap.get(this.QUALIFICATION);
      this.filterCost += this.filterMap.get(this.QUALIFICATION);
    }
    this.updateJobCost();

  }
  removeQualificationCost() {
    if (this.qualifications && this.qualifications.length === 0) {
      this.qualificationCost -= this.filterMap.get(this.QUALIFICATION);
      this.filterCost -= this.filterMap.get(this.QUALIFICATION);
    }
    this.updateJobCost();
  }

  addCourseCost() {
    // console.log("Qualifications are "+this.qualifications);
    if (this.courses && this.courses.length <= 1) {
      this.coursesCost += this.filterMap.get(this.COURSE);
      this.filterCost += this.filterMap.get(this.COURSE);
    }
    this.updateJobCost();

  }

  removeCourseCost() {
    if (this.courses && this.courses.length === 0) {
      this.coursesCost -= this.filterMap.get(this.COURSE);
      this.filterCost -= this.filterMap.get(this.COURSE);
    }
    this.updateJobCost();
  }

  updateGenderCost() {
    if (this.gender && !this.genderAdded) {
      this.genderCost += this.filterMap.get(this.GENDER);
      this.filterCost += this.filterMap.get(this.GENDER);
      this.genderAdded = true;
    } else if (!this.gender && this.genderAdded) {
      this.genderCost -= this.filterMap.get(this.GENDER);
      this.filterCost -= this.filterMap.get(this.GENDER);
      this.genderAdded = false;
    }
    this.updateJobCost();
  }

  updateGraduationDateCost() {
    if (this.graduationDate) {
      this.gradDate = new Date(this.graduationDate.year + '-' + this.graduationDate.month + '-' + this.graduationDate.day);
    }
    if (this.graduationDate && !this.graduationDateAdded) {
      this.gradDateCost += this.filterMap.get(this.GRADUATION_DATE);
      this.filterCost += this.filterMap.get(this.GRADUATION_DATE);
      this.graduationDateAdded = true;
    } else if (!this.graduationDate && this.graduationDateAdded) {
      this.graduationDateAdded = false;
      this.gradDateCost -= this.filterMap.get(this.GRADUATION_DATE);
      this.filterCost -= this.filterMap.get(this.GRADUATION_DATE);
    }

    this.updateJobCost();
  }

  validateDatesAndUpdateCost() {
    this.graduationDateAdded = false;
    this.endDateLesser = false;
    if (this.graduationToDate && this.graduationFromDate) {
      const fromDate = new Date(this.dateUtils
        .convertLocalDateToServer(this.graduationFromDate));
      const toDate = new Date(this.dateUtils
        .convertLocalDateToServer(this.graduationToDate));

      if (fromDate > toDate) {
        this.endDateLesser = true;
      } else {
        this.endDateLesser = false;
      }
    }
    this.updateGraduationMultiDateCosts();
  }

  updateGraduationMultiDateCosts() {
    if (this.graduationFromDate) {
      this.gradFromDate = new Date(this.graduationFromDate.year + '-' + this.graduationFromDate.month + '-' + this.graduationFromDate.day);
    }
    if (this.graduationToDate) {
      this.gradToDate = new Date(this.graduationToDate.year + '-' + this.graduationToDate.month + '-' + this.graduationToDate.day);
    }
    if (!this.endDateLesser) {
      if (this.graduationFromDate && this.graduationToDate && !(this.graduationToDateAdded && this.graduationFromDateAdded)) {
        this.gradDateCost += this.filterMap.get(this.GRADUATION_DATE);
        this.filterCost += this.filterMap.get(this.GRADUATION_DATE);
        this.graduationFromDateAdded = true;
        this.graduationToDateAdded = true;
      } else if (!(this.graduationFromDate && this.graduationToDate) && (this.graduationToDateAdded && this.graduationFromDateAdded)) {
        this.gradDateCost -= this.filterMap.get(this.GRADUATION_DATE);
        this.filterCost -= this.filterMap.get(this.GRADUATION_DATE);
        this.graduationFromDateAdded = false;
        this.graduationToDateAdded = false;

      }

    } else if (this.graduationToDateAdded && this.graduationFromDateAdded && this.endDateLesser) {
      this.gradDateCost -= this.filterMap.get(this.GRADUATION_DATE);
      this.filterCost -= this.filterMap.get(this.GRADUATION_DATE);
      this.graduationFromDateAdded = false;
      this.graduationToDateAdded = false;
    }
    this.updateJobCost();
  }

  enterPreview() {
    this.preview = true;

  }

  exitPreview() {
    this.preview = false;
  }

/*  offsetEscrow() {

    if (this.job.corporate.escrowAmount && this.useEscrow) {
      this.job.jobCost -= this.job.corporate.escrowAmount;
      this.job.corporate.escrowAmount -= this.job.corporate.escrowAmount;
    } else if (this.job.corporate.escrowAmount && !this.useEscrow) {
      this.job.jobCost += this.job.corporate.escrowAmount;
      this.job.corporate.escrowAmount += this.job.corporate.escrowAmount;
    }
  }
*/
  offsetEscrow() {
    console.log('value os check box escrow '+this.useEscrow);
     console.log('value local escrow fild '+this.escrowAmount);
    if (this.job.corporate.escrowAmount && this.useEscrow) {
      if (this.job.corporate.escrowAmount > this.job.jobCost) {
        this.job.corporate.escrowAmount -= this.job.jobCost;
        this.job.jobCost -= this.job.jobCost;
        this.job.escrowAmountUsed = this.job.jobCost;
      } else if (this.job.corporate.escrowAmount < this.job.jobCost) {
        this.job.jobCost -= this.job.corporate.escrowAmount;
        this.job.corporate.escrowAmount -= this.job.corporate.escrowAmount;
        this.job.escrowAmountUsed = this.job.corporate.escrowAmount;
      }
    } else if ( !this.useEscrow) {
      console.log('in else');
      this.job.jobCost += this.escrowAmount;
      this.job.corporate.escrowAmount += this.escrowAmount;
      this.job.escrowAmountUsed -=this.escrowAmount;
    }
    console.log('copr amunt es'+this.job.corporate.escrowAmount);
    console.log('job costs'+this.job.jobCost);
  }

  clearDates() {
    this.graduationDateType = undefined;
    this.graduationFromDate = undefined;
    this.graduationToDate = undefined;
    this.gradToDate = undefined;
    this.gradFromDate = undefined;
    this.graduationDate = undefined;
    this.gradDate = undefined;
    this.singleDateControl = false;
    this.multipleDateControl = false;
    this.updateGraduationDateCost();
    this.updateGraduationMultiDateCosts();
  }

  calculateDiscountedCost() {
    this.setPayment();
    this.discountedJobCost = (this.job.jobCost * this.filterMap.get(this.DISCOUNT) / 100);
  }

  applyDiscount() {
    this.job.jobCost -= this.discountedJobCost;
  }

  revertDiscount() {
    this.job.jobCost += this.discountedJobCost;
  }

  showDateControl() {
    this.singleDateControl = false;
    this.multipleDateControl = false;
    if (this.graduationDateType === 'less' || this.graduationDateType === 'greater') {
      this.singleDateControl = true;
      this.graduationFromDate = undefined;
      this.graduationToDate = undefined;
      this.gradToDate = undefined;
      this.gradFromDate = undefined;
      this.updateGraduationDateCost();
    } else if (this.graduationDateType === 'between') {
      this.multipleDateControl = true;
      this.graduationDate = undefined;
      this.gradDate = undefined;
      this.updateGraduationMultiDateCosts();

    }

  }
  addLanguageCost() {
    if (this.languages && this.languages.length <= 1) {
      this.languageCost += this.filterMap.get(this.LANGUAGE);
      this.filterCost += this.filterMap.get(this.LANGUAGE);
    }
    this.updateJobCost();

  }

  removeLanguageCost() {
    if (this.languages && this.languages.length === 0) {
      this.languageCost -= this.filterMap.get(this.LANGUAGE);
      this.filterCost -= this.filterMap.get(this.LANGUAGE);
    }
    this.updateJobCost();
  }

  updateJobCost() {
    console.log('Filter cost is =======' + JSON.stringify(this.filterCost));
    this.job.jobCost = this.job.noOfApplicants * (this.filterCost);
    if (this.prevJobCost) {
      this.jobCostDifference = this.job.jobCost - this.prevJobCost;
      if (this.jobCostDifference < 0) {
        this.job.corporate.escrowAmount = this.absoluteValue(this.jobCostDifference);
        this.job.jobCost = this.prevJobCost;
      }

    }
  }

  finalJobCost() {
    if (this.job.corporate.escrowAmount) {
      this.job.jobCost = - this.job.corporate.escrowAmount;
    }
  }

  absoluteValue(value) {
    return Math.abs(value);
  }
  captureEmploymentTypeCost(value) {
    if (value) {
      if (this.employmentTypeCost.length === 0) {
        this.employmentTypeCost.push(value.employmentTypeCost)
        this.filterCost = this.filterCost + value.employmentTypeCost;
      } else if (this.employmentTypeCost.length > 0) {
        this.filterCost = this.filterCost - this.employmentTypeCost.pop();
        console.log('filter cost employment type ' + JSON.stringify(this.filterCost));
        this.employmentTypeCost.push(value.employmentTypeCost)
        this.filterCost = this.filterCost + value.employmentTypeCost;
      }
    } else {
      this.filterCost = this.filterCost - this.employmentTypeCost.pop();
    }
    this.updateJobCost();
  }

  captureJobTypeCost(value) {
    if (value) {
      if (this.jobTypeCost.length === 0) {
        this.jobTypeCost.push(value.jobTypeCost)
        this.filterCost = this.filterCost + value.jobTypeCost;
      } else if (this.jobTypeCost.length > 0) {
        this.filterCost = this.filterCost - this.jobTypeCost.pop();
        this.jobTypeCost.push(value.jobTypeCost)
        this.filterCost = this.filterCost + value.jobTypeCost;
      }
    } else {
      this.filterCost = this.filterCost - this.jobTypeCost.pop();
    }
    this.updateJobCost();
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }

  prepareJobFilter() {
    this.clearFilterDescription();
    this.jobFilter.filterDescription = {};
    this.jobFilter.filterDescription.basic = this.basic;
    if (this.colleges && this.colleges.length > 0) {
      this.jobFilter.filterDescription.colleges = this.colleges;
    }
    if (this.universities && this.universities.length > 0) {
      this.jobFilter.filterDescription.universities = this.universities;
    }
    if (this.premium) {
      this.jobFilter.filterDescription.premium = this.premium;
      if (this.courses && this.courses.length > 0) {
        this.jobFilter.filterDescription.courses = this.courses;
      }
      if (this.qualifications && this.qualifications.length > 0) {
        this.jobFilter.filterDescription.qualifications = this.qualifications;
      }
      if (this.scoreType) {
        this.jobFilter.filterDescription.scoreType = this.scoreType;
      }
      if (this.percentage) {
        this.jobFilter.filterDescription.percentage = this.percentage;
      }
      if (this.roundOfGrade) {
        this.jobFilter.filterDescription.gpa = this.roundOfGrade + '.' + this.gradeDecimal;
      }
    }
    if (this.addOn) {
      this.jobFilter.filterDescription.addOn = this.addOn;
      if (this.graduationDateType) {
        this.jobFilter.filterDescription.graduationDateType = this.graduationDateType;
        if ((this.graduationDateType === 'less') || (this.graduationDateType === 'greater')) {
          this.jobFilter.filterDescription.graduationDate = this.graduationDate;
        } else if (this.graduationDateType === 'between') {
          this.jobFilter.filterDescription.graduationFromDate = this.graduationFromDate;
          this.jobFilter.filterDescription.graduationToDate = this.graduationToDate;
        }
      }
      if (this.languages && this.languages.length > 0) {
        this.jobFilter.filterDescription.languages = this.languages;
      }
      if (this.gender) {
        this.jobFilter.filterDescription.gender = this.gender;
      }
    }
    if (this.job.jobFilters && this.job.jobFilters.length > 0) {
      this.job.jobFilters.forEach((jobFilter) => {
        jobFilter.filterDescription = this.jobFilter.filterDescription;
      });
    } else {
      this.job.jobFilters = [];
      this.job.jobFilters.push(this.jobFilter);
    }

    console.log('job filter is ' + JSON.stringify(this.job.jobFilters));
  }

  clearFilterDescription() {
    if (this.job.jobFilters) {
      this.job.jobFilters.forEach((jobFilter) => {
        jobFilter.filterDescription = '';
      });
    }

  }

  setAmountPaid() {
    if (this.job.paymentType === PaymentType.UPFRONT) {
      this.job.amountPaid = this.job.jobCost;
    }
  }

  save(saveType) {
    console.log('Job ststaus is' + saveType);
    this.isSaving = true;
    if (saveType === JobConstants.ACTIVE) {
      this.prepareJobFilter();
    }
    console.log('Prev job is ' + JSON.stringify(this.prevJob));
    console.log('Current job is ' + JSON.stringify(this.job));
    this.job.jobTitle = this.job.jobTitle.trim();
    this.job.jobDescription = this.job.jobDescription.trim();
    if (this.discountedJobCost && this.discountedJobCost > 0) {
      this.applyDiscount();
    }
    this.setAmountPaid();
    if (!this.jobEquals(this.job, this.prevJob)
      || !this.jobFilterEquals(this.job, this.prevJob)) {
      console.log('Am i in ');
      this.job.jobStatus = saveType;
      if (this.job.id !== undefined) {
        this.job.updatedBy = this.account.id;
        this.removeDates(this.job);
        this.subscribeToSaveResponse(
          this.jobService.update(this.job), saveType);
      } else {
        this.job.createdBy = this.account.id;
        this.removeDates(this.job);
        this.subscribeToSaveResponse(
          this.jobService.create(this.job), saveType);
      }
    } else if (saveType === 1) {
      this.activeModal.dismiss();
    }

  }

  private jobFilterEquals(currentJob, previousJob) {
    if (!previousJob) {
      return false;
    }
    if (
      equal(JSON.stringify(currentJob.jobFilters), JSON.stringify(previousJob.jobFilters)) &&
      equal(JSON.stringify(currentJob.employmentType), JSON.stringify(previousJob.employmentType)) &&
      equal(JSON.stringify(currentJob.jobType), JSON.stringify(previousJob.jobType)) &&
      equal(JSON.stringify(currentJob.noOfApplicants), JSON.stringify(previousJob.noOfApplicants))) {
      console.log('filter equals');
      this.isSaving = false;
      return true;
    } else {
      return false;
    }
  }


  private jobEquals(currentJob, previousJob) {
    if (previousJob == null) {
      return false;
    }
    if ((equal(JSON.stringify(currentJob.jobTitle), JSON.stringify(previousJob.jobTitle))) &&
      (equal(JSON.stringify(currentJob.jobDescription), JSON.stringify(previousJob.jobDescription))) &&
      (equal(JSON.stringify(currentJob.salary), JSON.stringify(previousJob.salary)))) {
      console.log('JOB equals');
      this.isSaving = false;
      return true;
    } else {
      return false;
    }

  }

  removeDates(job: Job) {
    job.createDate = undefined;
    job.updateDate = undefined;
  }
  private subscribeToSaveResponse(result: Observable<Job>, saveType) {
    result.subscribe((res: Job) =>
      this.onSaveSuccess(res, saveType), (res: Response) => this.onSaveError(res));
  }

  private onSaveSuccess(result: Job, saveType) {
    if (result.jobFilters && result.jobFilters.length === 0) {
      result.jobFilters = null;
    }
    this.job = result;
    // console.log('result is ' + JSON.stringify(this.job));
    this.prevJob = deepcopy(result);
    console.log('result is ' + JSON.stringify(this.job));
    this.eventManager.broadcast({name: 'jobListModification', content: 'OK'});
    this.isSaving = false;

    if (saveType === JobConstants.ACTIVE || this.principal.hasAnyAuthorityDirect([AuthoritiesConstants.ADMIN])) {
      this.activeModal.dismiss(result);
    }
  }

  private onSaveError(error: any) {
    console.log('in error' + JSON.stringify(error));
    this.isSaving = false;
    this.onError(error);
  }

  private onError(error: any) {
    this.router.navigate(['/error']);
    this.activeModal.dismiss();
    this.jhiAlertService.error(error.message, null, null);
  }

  trackJobTypeById(index: number, item: JobType) {
    return item.id;
  }

  trackEmploymentTypeById(index: number, item: EmploymentType) {
    return item.id;
  }

  trackCorporateById(index: number, item: Corporate) {
    return item.id;
  }

  trackCandidateById(index: number, item: Candidate) {
    return item.id;
  }

  requestCollegeData = (text: string): Observable<Response> => {
    return this.collegeService.searchRemote({
      query: text
    }).map((data) => data.json());
  }

  requestQualificationData = (text: string): Observable<Response> => {
    return this.qualificationService.searchRemote({
      query: text
    }).map((data) => data.json());
  }

  requestCourseData = (text: string): Observable<Response> => {
    return this.courseService.searchRemote({
      query: text
    }).map((data) => data.json());
  }

  requestLanguageData = (text: string): Observable<Response> => {
    return this.languageService.searchRemote({
      query: text
    }).map((data) => data.json());
  }

  requestUniversityData = (text: string): Observable<Response> => {
    return this.universityService.searchRemote({
      query: text
    }).map((data) => data.json());
  }




  getSelected(selectedVals: Array<any>, option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}

@Component({
  selector: 'jhi-job-popup',
  template: ''
})
export class JobPopupComponent implements OnInit, OnDestroy {

  routeSub: any;

  constructor(
    private route: ActivatedRoute,
    private jobPopupService: JobPopupService
  ) {}

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      if (params['id']) {
        this.jobPopupService
          .open(JobDialogComponent as Component, params['id']);
      } else {
        this.jobPopupService
          .open(JobDialogComponent as Component);
      }
    });
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
  }
}


@Component({
  selector: 'jhi-job-popup',
  template: ''
})
export class JobPopupComponentNew implements OnInit, OnDestroy {

  routeSub: any;

  constructor(
    private route: ActivatedRoute,
    private jobPopupService: JobPopupServiceNew
  ) {}

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      this.jobPopupService
        .open(JobDialogComponent as Component, params['id']);
    });
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
  }
}
