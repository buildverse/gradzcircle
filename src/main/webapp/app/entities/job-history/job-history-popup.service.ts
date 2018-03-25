import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { JobHistory } from './job-history.model';
import { JobHistoryService } from './job-history.service';

@Injectable()
export class JobHistoryPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private jobHistoryService: JobHistoryService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.jobHistoryService.find(id).subscribe((jobHistory) => {
                    jobHistory.createDate = this.datePipe
                        .transform(jobHistory.createDate, 'yyyy-MM-ddTHH:mm:ss');
                    jobHistory.updateDate = this.datePipe
                        .transform(jobHistory.updateDate, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.jobHistoryModalRef(component, jobHistory);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.jobHistoryModalRef(component, new JobHistory());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    jobHistoryModalRef(component: Component, jobHistory: JobHistory): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.jobHistory = jobHistory;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
