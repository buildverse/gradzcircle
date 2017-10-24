import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CandidateNonAcademicWork } from './candidate-non-academic-work.model';
import { Candidate } from '../candidate/candidate.model';
import { CandidateNonAcademicWorkService } from './candidate-non-academic-work.service';

@Injectable()
export class CandidateNonAcademicWorkPopupServiceNew {
    private ngbModalRef: NgbModalRef;
    private candidateNonAcademicWork: CandidateNonAcademicWork;
    private candidate: Candidate;

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
        this.candidateNonAcademicWork = new CandidateNonAcademicWork();
        this.candidate = new Candidate();
        this.candidate.id = id;
        this.candidateNonAcademicWork.candidate = this.candidate;
        setTimeout(() => {
                    this.ngbModalRef = this.candidateNonAcademicWorkModalRef(component, this.candidateNonAcademicWork);
                    resolve(this.ngbModalRef);
                }, 0);
        
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
