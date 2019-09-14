import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import {CandidateEducation} from './candidate-education.model';
import {CandidateEducationService} from './candidate-education.service';
import {DataStorageService, Principal} from '../../shared';
import {AuthoritiesConstants} from '../../shared/authorities.constant';
import {CANDIDATE_ID, CANDIDATE_EDUCATION_ID, IS_EMPLOYMENT_PROJECT, CANDIDATE_PROJECT_ID, USER_ID} from '../../shared/constants/storage.constants';
import {NgxSpinnerService} from 'ngx-spinner';

@Component({
  selector: 'jhi-candidate-education',
  templateUrl: './candidate-education.component.html',
  styleUrls: ['candidate-education.css']
})
export class CandidateEducationComponent implements OnInit, OnDestroy {
  candidateEducations: CandidateEducation[];
  currentAccount: any;
  educationEventSubscriber: Subscription;
  projectEventSubscriber: Subscription;
  currentSearch: string;
  candidateId: any;
  subscription: Subscription;
  candidateEducationsForDisplay: CandidateEducation[];
  /*This will always be highest by default
   will chnage dynamically for display susequently
   */
  primaryCandidateEducation: CandidateEducation;

  constructor(
    private candidateEducationService: CandidateEducationService,
    private jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private activatedRoute: ActivatedRoute,
    private principal: Principal,
    private router: Router,
    private dataService: DataStorageService,
    private spinnerService: NgxSpinnerService
  ) {
    this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
      this.activatedRoute.snapshot.params['search'] : '';
  }

  setRouterAddEducationParams() {
    this.dataService.setdata(CANDIDATE_ID, this.candidateId);
  }

  setEducationRouteParam(event) {
    this.dataService.setdata(CANDIDATE_EDUCATION_ID, event);
  }

  setProjectRouteParam(event) {
    this.dataService.setdata(CANDIDATE_PROJECT_ID, event);
    this.dataService.setdata(IS_EMPLOYMENT_PROJECT, 'false');
  }

  loadAll() {
    if (this.currentSearch) {
      this.candidateEducationService.search({
        query: this.currentSearch,
      }).subscribe(
        (res: HttpResponse<CandidateEducation[]>) => this.candidateEducations = res.body,
        (res: HttpErrorResponse) => this.onError(res.message)
        );
      return;
    } else {
      this.candidateEducationService.query().subscribe(
        (res: HttpResponse<CandidateEducation[]>) => {
          this.candidateEducations = res.body;

          this.currentSearch = '';
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    }
  }
  /*To be removed once undertsand Elastic */
  loadEducationForCandidate() {
    this.spinnerService.show();
    this.candidateEducationService.findEducationByCandidateId(this.candidateId).subscribe(
      (res: HttpResponse<CandidateEducation[]>) => {
        this.candidateEducations = res.body;
        if (this.candidateEducations && this.candidateEducations.length <= 0) {
          this.router.navigate(['./candidate-profile']);
        }
        this.setPrimaryEducationOnLoad();
        this.spinnerService.hide();
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    return;
  }

  setPrimaryEducationOnLoad() {
    this.candidateEducationsForDisplay = [];
    this.primaryCandidateEducation = undefined;
   // console.log('candiate Educaiton from server is'+JSON.stringify(this.candidateEducations));
   // console.log('candiate Educaiton locally'+JSON.stringify(this.candidateEducationsForDisplay));
    if (this.candidateEducations && this.candidateEducations.length > 0) {
      this.candidateEducations.forEach((education) => {
        if (education.highestQualification && !this.primaryCandidateEducation) {
          this.primaryCandidateEducation = education;
        } else {
          this.candidateEducationsForDisplay.push(education);
        }
      });
    }
 //   console.log('candiate Educaiton from server in end is'+JSON.stringify(this.candidateEducations));
   // console.log('candiate Educaiton locally in end is '+JSON.stringify(this.candidateEducationsForDisplay));
  }

  setPrimaryByUserSelection(event) {
    this.candidateEducationsForDisplay = [];
     this.primaryCandidateEducation = undefined;
    if (this.candidateEducations && this.candidateEducations.length > 0) {
      this.candidateEducations.forEach((education) => {
        if (education.id === event) {
          this.primaryCandidateEducation = education;
        } else {
          this.candidateEducationsForDisplay.push(education);
        }
      });
    }
    window.scroll(0, 0);
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.loadAll();
  }

  clear() {
    this.currentSearch = '';
    this.loadAll();
  }

  ngOnInit() {
    this.candidateEducationsForDisplay = [];
    this.principal.identity().then((account) => {
      this.currentAccount = account;
      if (account.authorities.indexOf(AuthoritiesConstants.CANDIDATE) > -1) {
        this.candidateId = this.dataService.getData(USER_ID);
        this.currentSearch = this.candidateId;
        this.loadEducationForCandidate();
      } else {
        this.loadAll();
      }
      this.registerChangeInCandidateEducations();
    });
  }

  ngOnDestroy() {
    if(this.educationEventSubscriber) {
      this.eventManager.destroy(this.educationEventSubscriber);
    }
    if(this.projectEventSubscriber) {
    this.eventManager.destroy(this.projectEventSubscriber);
    }
  }

  trackId(index: number, item: CandidateEducation) {
    return item.id;
  }

  registerChangeInCandidateEducations() {
    if (this.candidateId) {
      this.educationEventSubscriber = this.eventManager.subscribe('candidateEducationListModification', (response) => this.loadEducationForCandidate());
      this.projectEventSubscriber = this.eventManager.subscribe('candidateProjectListModification', (response) => this.loadEducationForCandidate());
    } else {
      this.educationEventSubscriber = this.eventManager.subscribe('candidateEducationListModification', (response) => this.loadAll());
    }

  }

  private onError(error) {
    this.jhiAlertService.error(error.message, null, null);
  }
}
