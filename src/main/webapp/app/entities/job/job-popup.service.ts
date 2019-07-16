import {Injectable, Component} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {Job} from './job.model';
import {JobService} from './job.service';
import {HttpResponse} from '@angular/common/http';


@Injectable()
export class JobPopupService {
  private ngbModalRef: NgbModalRef;

  constructor(
    private modalService: NgbModal,
    private router: Router,
    private jobService: JobService

  ) {
    this.ngbModalRef = null;
  }

  open(component: Component, id?: number | any, matchScore?: number | any, hasCandidateApplied?: any): Promise<NgbModalRef> {
    return new Promise<NgbModalRef>((resolve, reject) => {
      const isOpen = this.ngbModalRef !== null;
      if (isOpen) {
        resolve(this.ngbModalRef);
      }

      if (id) {
        this.jobService.find(id)
          .subscribe((jobResponse: HttpResponse<Job>) => {
            const job: Job = jobResponse.body;
            job.matchScore = matchScore;
            if (job.createDate) {
              job.createDate = {
                year: job.createDate.getFullYear(),
                month: job.createDate.getMonth() + 1,
                day: job.createDate.getDate()
              };
            }
            if (job.updateDate) {
              job.updateDate = {
                year: job.updateDate.getFullYear(),
                month: job.updateDate.getMonth() + 1,
                day: job.updateDate.getDate()
              };
            }
            this.ngbModalRef = this.jobModalRef(component, job, hasCandidateApplied);
            resolve(this.ngbModalRef);
          });
      } else {
        // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
        setTimeout(() => {
          this.ngbModalRef = this.jobModalRef(component, new Job(), hasCandidateApplied);
          resolve(this.ngbModalRef);
        }, 0);
      }
    });
  }
  
  openForCandidateView(component: Component, jobId?: number | any, candidateId?: number | any): Promise<NgbModalRef> {
    return new Promise<NgbModalRef>((resolve, reject) => {
      const isOpen = this.ngbModalRef !== null;
      if (isOpen) {
        resolve(this.ngbModalRef);
      }

      if (jobId) {
        this.jobService.getJobForCandidateView(jobId,candidateId)
          .subscribe((jobResponse: HttpResponse<Job>) => {
            const job: Job = jobResponse.body;
            if (job.createDate) {
              job.createDate = {
                year: job.createDate.getFullYear(),
                month: job.createDate.getMonth() + 1,
                day: job.createDate.getDate()
              };
            }
            if (job.updateDate) {
              job.updateDate = {
                year: job.updateDate.getFullYear(),
                month: job.updateDate.getMonth() + 1,
                day: job.updateDate.getDate()
              };
            }
            this.ngbModalRef = this.jobModalRef(component, job);
            resolve(this.ngbModalRef);
          });
      } else {
        // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
        setTimeout(() => {
          this.ngbModalRef = this.jobModalRef(component, new Job());
          resolve(this.ngbModalRef);
        }, 0);
      }
    });
  }

  jobModalRef(component: Component, job: Job, hasCandidateApplied?: boolean): NgbModalRef {
    const modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.job = job;
  //  console.log('Job is '+JSON.stringify(job));
    modalRef.result.then((result) => {
      this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true, queryParamsHandling: 'merge'});
      this.ngbModalRef = null;
    }, (reason) => {
      this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true, queryParamsHandling: 'merge'});
      this.ngbModalRef = null;
    });
    return modalRef;
  }
}
