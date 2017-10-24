import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CandidateNonAcademicWork } from './candidate-non-academic-work.model';
import { CandidateNonAcademicWorkService } from './candidate-non-academic-work.service';

@Injectable()
export class CandidateNonAcademicWorkPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private candidateNonAcademicWorkService: CandidateNonAcademicWorkService

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
                this.candidateNonAcademicWorkService.find(id).subscribe((candidateNonAcademicWork) => {
                    if (candidateNonAcademicWork.nonAcademicWorkStartDate) {
                        candidateNonAcademicWork.nonAcademicWorkStartDate = {
                            year: candidateNonAcademicWork.nonAcademicWorkStartDate.getFullYear(),
                            month: candidateNonAcademicWork.nonAcademicWorkStartDate.getMonth() + 1,
                            day: candidateNonAcademicWork.nonAcademicWorkStartDate.getDate()
                        };
                    }
                    if (candidateNonAcademicWork.nonAcademicWorkEndDate) {
                        candidateNonAcademicWork.nonAcademicWorkEndDate = {
                            year: candidateNonAcademicWork.nonAcademicWorkEndDate.getFullYear(),
                            month: candidateNonAcademicWork.nonAcademicWorkEndDate.getMonth() + 1,
                            day: candidateNonAcademicWork.nonAcademicWorkEndDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.candidateNonAcademicWorkModalRef(component, candidateNonAcademicWork);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.candidateNonAcademicWorkModalRef(component, new CandidateNonAcademicWork());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    candidateNonAcademicWorkModalRef(component: Component, candidateNonAcademicWork: CandidateNonAcademicWork): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.candidateNonAcademicWork = candidateNonAcademicWork;
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
