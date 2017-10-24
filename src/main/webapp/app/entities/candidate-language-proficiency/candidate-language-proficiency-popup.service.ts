import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CandidateLanguageProficiency } from './candidate-language-proficiency.model';
import { CandidateLanguageProficiencyService } from './candidate-language-proficiency.service';

@Injectable()
export class CandidateLanguageProficiencyPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private candidateLanguageProficiencyService: CandidateLanguageProficiencyService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any, languageLocked?:boolean): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.candidateLanguageProficiencyService.find(id).subscribe((candidateLanguageProficiency) => {
                    this.ngbModalRef = this.candidateLanguageProficiencyModalRef(component, candidateLanguageProficiency,languageLocked);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.candidateLanguageProficiencyModalRef(component, new CandidateLanguageProficiency(),languageLocked);
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    candidateLanguageProficiencyModalRef(component: Component, candidateLanguageProficiency: CandidateLanguageProficiency,languageLocked:boolean): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.candidateLanguageProficiency = candidateLanguageProficiency;
        modalRef.componentInstance.languageLocked = languageLocked;
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
