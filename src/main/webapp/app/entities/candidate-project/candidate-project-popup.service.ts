import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CandidateProject } from './candidate-project.model';
import { CandidateProjectService } from './candidate-project.service';
import { CandidateEmployment } from '../candidate-employment/candidate-employment.model';
import { CandidateEducation } from '../candidate-education/candidate-education.model';
import { HttpResponse } from '@angular/common/http';

import * as moment from 'moment';

@Injectable()
export class CandidateProjectPopupService {
    private ngbModalRef: NgbModalRef;
    private candidateEmployment: CandidateEmployment;
    private candidateEducation: CandidateEducation;
    constructor(private modalService: NgbModal, private router: Router, private candidateProjectService: CandidateProjectService) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any, isEmploymentProject?: boolean): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.candidateProjectService.find(id).subscribe((candidateProjectResponse: HttpResponse<CandidateProject>) => {
                    const candidateProject: CandidateProject = candidateProjectResponse.body;
                    if (candidateProject.projectStartDate) {
                        const d = new Date(candidateProject.projectStartDate);
                        candidateProject.projectStartDate = moment({
                            year: d.getFullYear(),
                            month: d.getMonth(),
                            day: d.getDate()
                        });
                    }
                    if (candidateProject.projectEndDate) {
                        const d = new Date(candidateProject.projectEndDate);
                        candidateProject.projectEndDate = moment({
                            year: d.getFullYear(),
                            month: d.getMonth(),
                            day: d.getDate()
                        });
                    }
                    if (candidateProject.educationId) {
                        this.candidateEducation = new CandidateEducation();
                        this.candidateEducation.id = candidateProject.educationId;
                        candidateProject.education = this.candidateEducation;
                    } else if (candidateProject.employmentId) {
                        this.candidateEmployment = new CandidateEmployment();
                        this.candidateEmployment.id = candidateProject.employmentId;
                        candidateProject.employment = this.candidateEmployment;
                    }
                    this.ngbModalRef = this.candidateProjectModalRef(component, candidateProject, isEmploymentProject);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.candidateProjectModalRef(component, new CandidateProject(), isEmploymentProject);
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    candidateProjectModalRef(component: Component, candidateProject: CandidateProject, isEmploymentProject: boolean): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.candidateProject = candidateProject;
        modalRef.componentInstance.isEmploymentProject = isEmploymentProject;
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
