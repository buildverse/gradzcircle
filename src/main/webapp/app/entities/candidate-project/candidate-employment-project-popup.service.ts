import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CandidateProject } from './candidate-project.model';
import { CandidateProjectService } from './candidate-project.service';
import { CandidateEmployment } from '../candidate-employment/candidate-employment.model';

@Injectable()
export class CandidateEmploymentProjectPopupService {
    private isOpen = false;
    private ngbModalRef: NgbModalRef;
    private candidateProject: CandidateProject;
    private candidateEmployment: CandidateEmployment;
    constructor(private modalService: NgbModal, private router: Router, private candidateProjectService: CandidateProjectService) {}

    open(component: Component, id?: number | any, isEmploymentProject?: boolean): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }
            this.isOpen = true;
            this.candidateProject = new CandidateProject();
            this.candidateEmployment = new CandidateEmployment();
            this.candidateEmployment.id = id;
            this.candidateProject.employment = this.candidateEmployment;
            setTimeout(() => {
                this.ngbModalRef = this.candidateProjectModalRef(component, this.candidateProject, isEmploymentProject);
                resolve(this.ngbModalRef);
            }, 0);
        });
    }

    candidateProjectModalRef(component: Component, candidateProject: CandidateProject, isEmploymentProject: boolean): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.candidateProject = candidateProject;
        modalRef.componentInstance.isEmploymentProject = isEmploymentProject;
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
