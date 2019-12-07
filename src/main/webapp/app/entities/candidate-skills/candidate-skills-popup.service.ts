import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { CandidateSkills } from './candidate-skills.model';
import { CandidateSkillsService } from './candidate-skills.service';

@Injectable()
export class CandidateSkillsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(private modalService: NgbModal, private router: Router, private candidateSkillsService: CandidateSkillsService) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.candidateSkillsService.find(id).subscribe((candidateSkillsResponse: HttpResponse<CandidateSkills>) => {
                    const candidateSkills: CandidateSkills = candidateSkillsResponse.body;
                    this.ngbModalRef = this.candidateSkillsModalRef(component, candidateSkills);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.candidateSkillsModalRef(component, new CandidateSkills());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    candidateSkillsModalRef(component: Component, candidateSkills: CandidateSkills): NgbModalRef {
        // console.log('candidate skills aer ========='+JSON.stringify(candidateSkills));
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.candidateSkills = candidateSkills;
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
