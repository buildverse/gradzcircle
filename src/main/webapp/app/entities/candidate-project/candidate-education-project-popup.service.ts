import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CandidateProject } from './candidate-project.model';
import { CandidateProjectService } from './candidate-project.service';
import { CandidateEducation } from '../candidate-education/candidate-education.model';

@Injectable()
export class CandidateEducationProjectPopupService {
    private isOpen = false;
    private ngbModalRef: NgbModalRef;
    private candidateProject: CandidateProject;
    private candidateEducation: CandidateEducation;
    constructor(private modalService: NgbModal, private router: Router, private candidateProjectService: CandidateProjectService) {}

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }
            this.isOpen = true;
            this.candidateProject = new CandidateProject();
            this.candidateEducation = new CandidateEducation();
            this.candidateEducation.id = id;
            this.candidateProject.education = this.candidateEducation;
            setTimeout(() => {
                this.ngbModalRef = this.candidateProjectModalRef(component, this.candidateProject);
                resolve(this.ngbModalRef);
            }, 0);
        });
    }

    candidateProjectModalRef(component: Component, candidateProject: CandidateProject): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.candidateProject = candidateProject;

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
