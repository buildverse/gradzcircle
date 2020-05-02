import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Job } from '../../shared/job-common/job.model';
import { JobService } from './job.service';
import { HttpResponse } from '@angular/common/http';
import * as moment from 'moment';

@Injectable()
export class JobPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(private modalService: NgbModal, private router: Router, private jobService: JobService) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any, matchScore?: number | any, hasCandidateApplied?: any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.jobService.find(id).subscribe((jobResponse: HttpResponse<Job>) => {
                    const job: Job = jobResponse.body;
                    job.matchScore = matchScore;
                    if (job.createDate) {
                        const d = new Date(job.createDate);
                        job.createDate = moment({
                            year: d.getFullYear(),
                            month: d.getMonth() + 1,
                            day: d.getDate()
                        });
                    }
                    if (job.updateDate) {
                        const d = new Date(job.updateDate);
                        job.updateDate = moment({
                            year: d.getFullYear(),
                            month: d.getMonth() + 1,
                            day: d.getDate()
                        });
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

    openForCandidateView(component: Component, jobId?: number | any, candidateId?: number | any, profile?: string): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (jobId) {
                this.jobService.getJobForCandidateView(jobId, candidateId).subscribe((jobResponse: HttpResponse<Job>) => {
                    const job: Job = jobResponse.body;
                    if (job.createDate) {
                        const d = new Date(job.createDate);
                        job.createDate = moment({
                            year: d.getFullYear(),
                            month: d.getMonth() + 1,
                            day: d.getDate()
                        });
                    }
                    if (job.updateDate) {
                        const d = new Date(job.updateDate);
                        job.updateDate = moment({
                            year: d.getFullYear(),
                            month: d.getMonth() + 1,
                            day: d.getDate()
                        });
                    }
                    this.ngbModalRef = this.jobModalRef(component, job, profile);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.jobModalRef(component, new Job(), profile);
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    jobModalRef(component: Component, job: Job, profile?: string): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.job = job;
        modalRef.componentInstance.profile = profile;
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
