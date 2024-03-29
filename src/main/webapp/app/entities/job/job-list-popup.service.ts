import { JobListForLinkedCandidate } from './job-list-linked-candidate.model';
import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JobService } from './job.service';
import { HttpResponse } from '@angular/common/http';

@Injectable()
export class JobListPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(private modalService: NgbModal, private router: Router, private jobService: JobService) {
        this.ngbModalRef = null;
    }

    open(component: Component, candidateId?: number | any, corporateId?: any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (candidateId && corporateId) {
                this.jobService
                    .queryJobListForLinkedCandidates({}, candidateId, corporateId)
                    .subscribe((jobResponse: HttpResponse<JobListForLinkedCandidate[]>) => {
                        const jobList: JobListForLinkedCandidate[] = jobResponse.body;

                        this.ngbModalRef = this.jobModalRef(component, jobList);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.jobModalRef(component, new Array());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    jobModalRef(component: Component, jobList: JobListForLinkedCandidate[]): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.jobList = jobList;
        modalRef.result.then(
            result => {
                this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalRef = null;
            },
            reason => {
                this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalRef = null;
            }
        );
        return modalRef;
    }
}
