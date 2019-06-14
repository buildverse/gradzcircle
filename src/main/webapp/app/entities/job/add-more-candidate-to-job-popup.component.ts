import {JOB_ID, BUSINESS_PLAN_ENABLED} from '../../shared/constants/storage.constants';
import {DataStorageService} from '../../shared/helper/localstorage.service';
import {AddMoreCandidatesToPopupService} from './add-more-canddiate-to-job-popup.service';
import {Job} from './job.model';
import {JobService} from './job.service';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {NgxSpinnerService} from 'ngx-spinner';
import {Corporate} from '../corporate/corporate.model';
import { JhiEventManager } from 'ng-jhipster';

@Component({
  selector: 'jhi-add-more-candidates-dialog',
  templateUrl: './add-more-candidates.html'
})
export class AddMoreCandidatesToJobComponent implements OnInit {
  addtionalCandidates: number;
  isSaving: boolean;
  newJobCost: number;
  useEscrow: boolean;
  businessPlanEnabled: boolean;
  costDiff: number;
  isEscrowCheckBoxDisabled: boolean;
  transientCorpEscrowAmount: number;
  escrowAmountUsed: number;
  job: Job;

  constructor(
    public activeModal: NgbActiveModal,
    public localStorageService: DataStorageService,
    public jobService: JobService,
    private spinnerService: NgxSpinnerService,
    private router: Router,
     private eventManager: JhiEventManager,
  ) {
    this.businessPlanEnabled = this.localStorageService.getData(BUSINESS_PLAN_ENABLED) === 'false' ? false : true;

  }


  ngOnInit() {
    this.costDiff = undefined;
    this.isEscrowCheckBoxDisabled = true;
    this.transientCorpEscrowAmount = this.job.corporateEscrowAmount;
    this.newJobCost = 0;
  
  }

  updateJobCost() {
    if (!this.addtionalCandidates) {
      this.isEscrowCheckBoxDisabled = true;
      this.useEscrow = false;
      this.newJobCost = 0;
      this.offsetEscrow();
    }

    if (this.addtionalCandidates && this.addtionalCandidates > 0) {
      this.isEscrowCheckBoxDisabled = false;
      this.newJobCost = this.job.filterCost * this.addtionalCandidates;
      this.offsetEscrow();
    }

  }

  offsetEscrow(event?: any) {
    this.useEscrow = event;
    if (this.useEscrow) {
      this.costDiff = this.newJobCost - this.job.corporateEscrowAmount; // Payable amount

      if (this.costDiff >= 0) {
        this.costDiff = this.newJobCost - this.job.corporateEscrowAmount; // Payable amount
        this.transientCorpEscrowAmount = 0;
        this.escrowAmountUsed = this.job.corporateEscrowAmount;
      } else if (this.costDiff < 0) {
        this.transientCorpEscrowAmount = this.job.corporateEscrowAmount - this.newJobCost;
        this.escrowAmountUsed = this.job.corporateEscrowAmount + this.costDiff;
      }
    } else if (!this.useEscrow) {
      this.transientCorpEscrowAmount = this.job.corporateEscrowAmount;
      this.costDiff = this.newJobCost;// Amount Payable
      this.escrowAmountUsed = 0;
    }
  }

  updateJob() {
    console.log('Fiansl poayment is ' + this.costDiff);
    console.log('Fiansl escrow amount  is ' + this.transientCorpEscrowAmount);
    console.log(' escrow amount used is ' + this.escrowAmountUsed);
       //console.log('Job Id is ' + this.jobId);
    this.job.escrowAmountUsed = this.escrowAmountUsed;
  //  this.job.id = this.jobId;
    this.job.noOfApplicants = this.addtionalCandidates;
    this.job.amountPaid = this.costDiff;
    this.job.corporate = new Corporate();
    this.job.corporate.escrowAmount = this.transientCorpEscrowAmount;
    this.jobService.addtionalCandidatesForJob(this.job).subscribe((res: HttpResponse<Job>) => {
     this.onSaveSuccess(res.body);
    },
      (res: HttpErrorResponse) => this.onSaveError(res));

  }
  
   private onSaveSuccess(result: Job) {
  
    this.job = result;
   
    this.eventManager.broadcast({name: 'jobListModification', content: 'OK'});
    this.isSaving = false;
    this.spinnerService.hide();
    this.activeModal.dismiss(result);

  }

  private onSaveError(error: any) {
    //console.log('in error' + JSON.stringify(error));
    this.isSaving = false;
    this.spinnerService.hide();
    this.onError(error);
  }

  private onError(error: any) {
    this.router.navigate(['/error']);
    this.activeModal.dismiss();
    //  this.jhiAlertService.error(error.message, null, null);
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }
}

@Component({
  selector: 'jhi-add-more-candidates-popup',
  template: ''
})
export class AddMoreCandidatesToJobPopUpComponent implements OnInit, OnDestroy {

  routeSub: any;

  constructor(
    private route: ActivatedRoute,
    private addCandidateToJobPopupService: AddMoreCandidatesToPopupService,
    private localStorageService: DataStorageService
  ) {}

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      const jobId = this.localStorageService.getData(JOB_ID);
      this.addCandidateToJobPopupService
        .open(AddMoreCandidatesToJobComponent as Component, jobId);
    });
  }


  ngOnDestroy() {
    this.routeSub.unsubscribe();
  }
}
