import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CandidateProject } from './candidate-project.model';
import { CandidateProjectService } from './candidate-project.service';
import { CandidateEmployment } from '../candidate-employment/candidate-employment.model';
import { CandidateEducation } from '../candidate-education/candidate-education.model';
@Injectable()
export class CandidateProjectPopupService {
    private ngbModalRef: NgbModalRef;
    private candidateEmployment : CandidateEmployment;
    private candidateEducation : CandidateEducation;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private candidateProjectService: CandidateProjectService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any,isEmploymentProject?:boolean): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.candidateProjectService.find(id).subscribe((candidateProject) => {
                    if (candidateProject.projectStartDate) {
                        candidateProject.projectStartDate = {
                            year: candidateProject.projectStartDate.getFullYear(),
                            month: candidateProject.projectStartDate.getMonth() + 1,
                            day: candidateProject.projectStartDate.getDate()
                        };
                    }
                    if (candidateProject.projectEndDate) {
                        candidateProject.projectEndDate = {
                            year: candidateProject.projectEndDate.getFullYear(),
                            month: candidateProject.projectEndDate.getMonth() + 1,
                            day: candidateProject.projectEndDate.getDate()
                        };
                    }
                    if ( candidateProject.education ){
                        this.candidateEducation = new CandidateEducation();
                        this.candidateEducation.id = candidateProject.education.id;
                        candidateProject.education = this.candidateEducation;
                    }
                    else if( candidateProject.employment ) {
                        this.candidateEmployment = new CandidateEmployment();
                        this.candidateEmployment.id = candidateProject.employment.id;
                        candidateProject.employment = this.candidateEmployment;
                    }
                    this.ngbModalRef = this.candidateProjectModalRef(component, candidateProject,isEmploymentProject);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.candidateProjectModalRef(component, new CandidateProject(),isEmploymentProject);
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    candidateProjectModalRef(component: Component, candidateProject: CandidateProject,isEmploymentProject: boolean): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.candidateProject = candidateProject;
        modalRef.componentInstance.isEmploymentProject = isEmploymentProject;
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
