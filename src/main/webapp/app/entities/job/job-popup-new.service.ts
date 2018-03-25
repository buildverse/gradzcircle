import {Injectable, Component} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {Job} from './job.model';
import {JobService} from './job.service';
import {Corporate} from '../corporate/corporate.model';
import {JobFilter} from '../job-filter/job-filter.model';

@Injectable()
export class JobPopupServiceNew {
  private ngbModalRef: NgbModalRef;
  private job: Job;
  private corporate: Corporate;

  constructor(
    private modalService: NgbModal,
    private router: Router,
    private jobService: JobService

  ) {
    this.ngbModalRef = null;
  }

  open(component: Component, id?: number | any): Promise<NgbModalRef> {
    return new Promise<NgbModalRef>((resolve, reject) => {
      const isOpen = this.ngbModalRef !== null;
      if (isOpen) {
        resolve(this.ngbModalRef);
      }

      this.job = new Job();
      this.job.corporate = new Corporate();
      this.job.corporate.id = id;
      // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
      setTimeout(() => {
        this.ngbModalRef = this.jobModalRef(component, this.job);
        resolve(this.ngbModalRef);
      }, 0);

    });
  }

  jobModalRef(component: Component, job: Job): NgbModalRef {
    const modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.job = job;
    modalRef.result.then((result) => {
      this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
      this.ngbModalRef = null;
    }, (reason) => {
      this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
      this.ngbModalRef = null;
    });
    return modalRef;
  }
}
