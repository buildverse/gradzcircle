import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CandidateEducation } from './candidate-education.model';
import { CandidateEducationService } from './candidate-education.service';
import { College } from '../../entities/college/college.model';
import { Course } from '../../entities/course/course.model';
import { Qualification } from '../../entities/qualification/qualification.model';
import { Candidate } from '../candidate/candidate.model';
import { HttpResponse } from '@angular/common/http';
import * as moment from 'moment';
@Injectable()
export class CandidateEducationPopupService {
    private ngbModalRef: NgbModalRef;
    private candidate: Candidate;

    constructor(private modalService: NgbModal, private router: Router, private candidateEducationService: CandidateEducationService) {
        this.ngbModalRef = null;
    }

    open(
        component: Component,
        id?: number | any,
        courses?: Course[],
        colleges?: College[],
        qualifications?: Qualification[]
    ): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.candidateEducationService.find(id).subscribe((candidateEducationResponse: HttpResponse<CandidateEducation>) => {
                    const candidateEducation: CandidateEducation = candidateEducationResponse.body;
                    if (candidateEducation.educationFromDate) {
                        const d = new Date(candidateEducation.educationFromDate);
                        candidateEducation.educationFromDate = moment({
                            year: d.getFullYear(),
                            month: d.getMonth(),
                            day: d.getDate()
                        });
                    }
                    if (candidateEducation.educationToDate) {
                        const d = new Date(candidateEducation.educationToDate);
                        candidateEducation.educationToDate = moment({
                            year: d.getFullYear(),
                            month: d.getMonth(),
                            day: d.getDate()
                        });
                    }
                    this.candidate = new Candidate();
                    this.candidate.id = candidateEducation.candidate.id;
                    candidateEducation.candidate = this.candidate;
                    candidateEducation.projects = null;
                    this.ngbModalRef = this.candidateEducationModalRef(component, candidateEducation, courses, colleges, qualifications);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.candidateEducationModalRef(component, new CandidateEducation());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    candidateEducationModalRef(
        component: Component,
        candidateEducation: CandidateEducation,
        courses?: Course[],
        colleges?: College[],
        qualifications?: Qualification[]
    ): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.candidateEducation = candidateEducation;
        modalRef.componentInstance.colleges = colleges;
        modalRef.componentInstance.qualifications = qualifications;
        modalRef.componentInstance.courses = courses;

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
