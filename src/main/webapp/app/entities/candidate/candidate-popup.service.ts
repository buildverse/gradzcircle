import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Candidate } from './candidate.model';
import { CandidateService } from './candidate.service';

@Injectable()
export class CandidatePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private candidateService: CandidateService

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
                this.candidateService.find(id)
                    .subscribe((candidateResponse: HttpResponse<Candidate>) => {
                        const candidate: Candidate = candidateResponse.body;
                        if (candidate.dateOfBirth) {
                            candidate.dateOfBirth = {
                                year: candidate.dateOfBirth.getFullYear(),
                                month: candidate.dateOfBirth.getMonth() + 1,
                                day: candidate.dateOfBirth.getDate()
                            };
                        }
                        this.ngbModalRef = this.candidateModalRef(component, candidate);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.candidateModalRef(component, new Candidate());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    candidateModalRef(component: Component, candidate: Candidate): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.candidate = candidate;
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
