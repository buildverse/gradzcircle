import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CandidateEmployment } from './candidate-employment.model';
import { CandidateEmploymentService } from './candidate-employment.service';
import { Qualification } from '../qualification';
import { Course } from '../course';
import { College } from '../college';
import { Candidate } from '../candidate/candidate.model';

@Injectable()
export class CandidateEmploymentPopupServiceNew {
    private isOpen = false;
    private candidateEmployment: CandidateEmployment;
    private candidate: Candidate;
    private ngbModalRef: NgbModalRef;
    constructor(private modalService: NgbModal, private router: Router, private candidateEmploymentService: CandidateEmploymentService) {}

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }
            this.isOpen = true;
            this.candidateEmployment = new CandidateEmployment();
            this.candidate = new Candidate();
            this.candidateEmployment.candidate = this.candidate;
            this.candidateEmployment.candidate.id = id;
            setTimeout(() => {
                this.ngbModalRef = this.candidateEducationModalRef(component, this.candidateEmployment);
                resolve(this.ngbModalRef);
            }, 0);
        });
    }

    candidateEducationModalRef(component: Component, candidateEmployment: CandidateEmployment): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.candidateEmployment = candidateEmployment;
        modalRef.result.then(
            result => {
                this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true });
                this.isOpen = false;
            },
            reason => {
                this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true });
                this.isOpen = false;
            }
        );
        return modalRef;
    }
}
