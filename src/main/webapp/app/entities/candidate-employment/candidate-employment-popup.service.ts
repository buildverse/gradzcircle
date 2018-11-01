import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CandidateEmployment } from './candidate-employment.model';
import { CandidateEmploymentService } from './candidate-employment.service';
import { HttpResponse } from '@angular/common/http';

@Injectable()
export class CandidateEmploymentPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private candidateEmploymentService: CandidateEmploymentService

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
                this.candidateEmploymentService.find(id)
                  .subscribe((candidateEmploymentResponse: HttpResponse<CandidateEmployment>) =>  {
                     const candidateEmployment: CandidateEmployment = candidateEmploymentResponse.body;
                    if (candidateEmployment.employmentStartDate) {
                        candidateEmployment.employmentStartDate = {
                            year: candidateEmployment.employmentStartDate.getFullYear(),
                            month: candidateEmployment.employmentStartDate.getMonth() + 1,
                            day: candidateEmployment.employmentStartDate.getDate()
                        };
                    }
                    if (candidateEmployment.employmentEndDate) {
                        candidateEmployment.employmentEndDate = {
                            year: candidateEmployment.employmentEndDate.getFullYear(),
                            month: candidateEmployment.employmentEndDate.getMonth() + 1,
                            day: candidateEmployment.employmentEndDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.candidateEmploymentModalRef(component, candidateEmployment);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.candidateEmploymentModalRef(component, new CandidateEmployment());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    candidateEmploymentModalRef(component: Component, candidateEmployment: CandidateEmployment): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.candidateEmployment = candidateEmployment;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
