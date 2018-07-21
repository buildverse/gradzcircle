import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';
import {JhiDateUtils} from 'ng-jhipster';
import {Job} from './job.model';
import {JobPopupService} from './job-popup.service';
import {JobService} from './job.service';

@Component({
  selector: 'jhi-job-view',
  templateUrl: './job-view.component.html'
})
export class JobViewComponent {

  job: Job;

  constructor(
    private jobService: JobService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    private dateUtils: JhiDateUtils
  ) {
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }

  load(id: number) {
    this.jobService.find(id).subscribe((job) => {
      this.changeFilterDates(job);
      this.job = job;
    });
  }

  private changeFilterDates(job: Job) {
    console.log('Converting ?');
    if (job.jobFilters !== null && job.jobFilters.length > 0) {
      if (job.jobFilters[0].filterDescription.graduationDate !== null) {
         console.log('converting '+JSON.stringify(this.dateUtils.convertLocalDateToServer(job.jobFilters[0].filterDescription.graduationToDate)));
        job.jobFilters[0].filterDescription.graduationDate = (this.dateUtils.convertLocalDateToServer(job.jobFilters[0].filterDescription.graduationDate));
      }
      if (job.jobFilters[0].filterDescription.graduationToDate !== null) {
        console.log('converting '+JSON.stringify(this.dateUtils.convertLocalDateToServer(job.jobFilters[0].filterDescription.graduationToDate)));
        job.jobFilters[0].filterDescription.graduationToDate = (this.dateUtils.convertLocalDateToServer(job.jobFilters[0].filterDescription.graduationToDate));
      }
      if (job.jobFilters[0].filterDescription.graduationFromDate !== null) {
        job.jobFilters[0].filterDescription.graduationFromDate = (this.dateUtils.convertLocalDateToServer(job.jobFilters[0].filterDescription.graduationFromDate));
      }
    }
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