import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CandidateEducation } from './candidate-education.model';
import { CandidateEducationService } from './candidate-education.service';
import { Qualification } from '../qualification';
import { Course } from '../course';
import { College } from '../college';
import { Candidate } from '../candidate/candidate.model';

@Injectable()
export class CandidateEducationPopupServiceNew {
    private isOpen = false;
    private candidateEducation: CandidateEducation;
    private candidate: Candidate;
    private ngbModalRef: NgbModalRef;
    constructor(private modalService: NgbModal, private router: Router, private candidateEducationService: CandidateEducationService) {}
    open(component: Component, id?: number | any, fromProfile?: boolean): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            this.candidateEducation = new CandidateEducation();
            this.candidate = new Candidate();
            this.candidateEducation.candidate = this.candidate;
            this.candidateEducation.candidate.id = id;
            setTimeout(() => {
                this.ngbModalRef = this.candidateEducationModalRef(component, this.candidateEducation, fromProfile);
                resolve(this.ngbModalRef);
            }, 0);
        });
    }

    candidateEducationModalRef(component: Component, candidateEducation: CandidateEducation, fromProfile: boolean): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.candidateEducation = candidateEducation;
        modalRef.componentInstance.fromProfile = fromProfile;
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
