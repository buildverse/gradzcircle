import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { CandidateCertification } from './candidate-certification.model';
import { CandidateCertificationService } from './candidate-certification.service';
import { Candidate } from '../candidate/candidate.model';
import * as moment from 'moment';

@Injectable()
export class CandidateCertificationPopupService {
    private ngbModalRef: NgbModalRef;
    private candidate: Candidate;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private candidateCertificationService: CandidateCertificationService
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
                this.candidateCertificationService
                    .find(id)
                    .subscribe((candidateCertificationResponse: HttpResponse<CandidateCertification>) => {
                        const candidateCertification: CandidateCertification = candidateCertificationResponse.body;
                        if (candidateCertification.certificationDate) {
                            const date = new Date(candidateCertification.certificationDate);
                            candidateCertification.certificationDate = moment({
                                year: date.getFullYear(),
                                month: date.getMonth(),
                                day: date.getDate()
                            });
                        }
                        this.candidate = new Candidate();
                        this.candidate.id = candidateCertification.candidate.id;
                        candidateCertification.candidate = this.candidate;
                        this.ngbModalRef = this.candidateCertificationModalRef(component, candidateCertification);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.candidateCertificationModalRef(component, new CandidateCertification());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    candidateCertificationModalRef(component: Component, candidateCertification: CandidateCertification): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.candidateCertification = candidateCertification;
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
