import {Component, OnInit, OnDestroy, Input} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {DataStorageService, Principal} from '../../shared';
import { JOB_ID, MATCH_SCORE } from '../../shared/constants/storage.constants';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';
import {JhiDateUtils} from 'ng-jhipster';
import {Job} from './job.model';
import {JobPopupService} from './job-popup.service';
import {JobService} from './job.service';
import { LoginModalService, LoginService} from '../../shared';
import { JobListEmitterService } from './job-list-change.service';
import {Observable} from 'rxjs/Rx';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-job-view',
  templateUrl: './job-view.component.html'
})
export class JobViewComponent implements OnInit {

  job: Job;
 //hasCandidateApplied: boolean;
  currentAccount: any;
  isSaving: boolean;
  modalRef: NgbModalRef;

  constructor(
    private jobService: JobService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    private dateUtils: JhiDateUtils,
    private principal: Principal,
    private router: Router,
    private jhiAlertService: JhiAlertService,
    private dataService: DataStorageService,
    private loginService: LoginService,
    private loginModalService: LoginModalService,
    private jobListEmitterService :JobListEmitterService,
  ) {
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }

  ngOnInit() {
    this.principal.identity().then((account) => {
      this.currentAccount = account;
    });
    //  console.log('Converting ?');
    if (this.job.jobFilters && this.job.jobFilters.length > 0) {
      if (this.job.jobFilters[0].filterDescription.graduationDate !== null) {
        //   console.log('converting '+JSON.stringify(this.dateUtils.convertLocalDateToServer(this.job.jobFilters[0].filterDescription.graduationToDate)));
        this.job.jobFilters[0].filterDescription.graduationDate = (this.dateUtils.convertLocalDateToServer(this.job.jobFilters[0].filterDescription.graduationDate));
      }
      if (this.job.jobFilters[0].filterDescription.graduationToDate !== null) {
        //   console.log('converting '+JSON.stringify(this.dateUtils.convertLocalDateToServer(this.job.jobFilters[0].filterDescription.graduationToDate)));
        this.job.jobFilters[0].filterDescription.graduationToDate = (this.dateUtils.convertLocalDateToServer(this.job.jobFilters[0].filterDescription.graduationToDate));
      }
      if (this.job.jobFilters[0].filterDescription.graduationFromDate !== null) {
        this.job.jobFilters[0].filterDescription.graduationFromDate = (this.dateUtils.convertLocalDateToServer(this.job.jobFilters[0].filterDescription.graduationFromDate));
      }
    }
  }

  login() {
    this.clear();
    this.modalRef = this.loginModalService.open();
  }
  
  applyForJob() {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.jobService.applyforJob(this.job.id,this.currentAccount.id));
  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<Job>>) {
    result.subscribe((res: HttpResponse<Job>) =>
      this.onSaveSuccess(res.body), (res: Response) => this.onSaveError(res));
  }

  private onSaveSuccess(result: Job) {
   this.activeModal.dismiss();
   this.eventManager.broadcast({name: 'jobListModification', content: 'OK'});
   this.jobListEmitterService.jobChanges(true);    
  }

  private onSaveError(error: any) {
    //console.log('in error' + JSON.stringify(error));
    //  this.isSaving = false;
    this.onError(error);
  }

  private onError(error: any) {
    this.router.navigate(['/error']);
    this.activeModal.dismiss();
    this.jhiAlertService.error(error.message, null, null);
  }
}


@Component({
  selector: 'jhi-job-view-popup',
  template: ''
})
export class JobViewPopupComponent implements OnInit, OnDestroy {

  routeSub: any;

  constructor(
    private route: ActivatedRoute,
    private jobPopupService: JobPopupService,
    private dataService: DataStorageService
  ) {}

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      if (params['id']) {
        this.jobPopupService
          .open(JobViewComponent as Component, params['id'], params['matchScore']);
      } else {
        let id = this.dataService.getData(JOB_ID);
        const matchScore = this.dataService.getData(MATCH_SCORE);
        if (!id) {
          id = this.dataService.getData(JOB_ID);

        }
        this.jobPopupService
          .open(JobViewComponent as Component, id, matchScore);
      }
    });
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
  }
}