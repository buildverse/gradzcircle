import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CandidateCertification } from './candidate-certification.model';
import { Candidate } from '../candidate/candidate.model';
import { CandidateCertificationService } from './candidate-certification.service';

@Injectable()
export class CandidateCertificationPopupServiceNew {
    private isOpen = false;
    private candidateCertification: CandidateCertification;
    private candidate: Candidate;
    private ngbModalRef: NgbModalRef;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private candidateCertificationService: CandidateCertificationService

    ) {}

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }
        this.isOpen = true;
        this.candidateCertification = new CandidateCertification();
        this.candidate = new Candidate();
        this.candidate.id = id;
        this.candidateCertification.candidate = this.candidate;
        setTimeout(() => {
                    this.ngbModalRef = this.candidateCertificationModalRef(component, this.candidateCertification);
                    resolve(this.ngbModalRef);
                }, 0);
        
        });
    }

    candidateCertificationModalRef(component: Component, candidateCertification: CandidateCertification): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.candidateCertification = candidateCertification;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
