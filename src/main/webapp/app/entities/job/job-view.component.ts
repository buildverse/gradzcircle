import {Component, OnInit, OnDestroy, Input} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ITEMS_PER_PAGE, Principal, ResponseWrapper} from '../../shared';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';
import {JhiDateUtils} from 'ng-jhipster';
import {Job} from './job.model';
import {JobPopupService} from './job-popup.service';
import {JobService} from './job.service';
import {of} from 'rxjs/observable/of';
import {Observable} from 'rxjs/Rx';

@Component({
  selector: 'jhi-job-view',
  templateUrl: './job-view.component.html'
})
export class JobViewComponent implements OnInit {

  job: Job;
 //hasCandidateApplied: boolean;
  currentAccount: any;
  isSaving: boolean;

  constructor(
    private jobService: JobService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    private dateUtils: JhiDateUtils,
    private principal: Principal,
    private router: Router,
    private jhiAlertService: JhiAlertService,
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
    if (this.job.jobFilters !== null && this.job.jobFilters.length > 0) {
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

  applyForJob() {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.jobService.applyforJob(this.job.id,this.currentAccount.id));
  }

  private subscribeToSaveResponse(result: Observable<Job>) {
    result.subscribe((res: Job) =>
      this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
  }

  private onSaveSuccess(result: Job) {
    this.activeModal.dismiss();
   this.eventManager.broadcast({name: 'jobListModification', content: 'OK'});
    //this.hasCandidateApplied = true;
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
    private jobPopupService: JobPopupService
  ) {}

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      this.jobPopupService
        .open(JobViewComponent as Component, params['id']);
    });
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
  }
}