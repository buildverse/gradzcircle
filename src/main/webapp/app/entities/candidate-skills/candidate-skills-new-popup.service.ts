import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService } from 'ng-jhipster';
import { Candidate } from '../../entities/candidate/candidate.model';
import { CandidateSkills } from './candidate-skills.model';

@Injectable()
export class CandidateSkillsPopupServiceNew {
    private ngbModalRef: NgbModalRef;
    private candidateSkills: CandidateSkills;
    private candidate: Candidate;
    private isOpen = false;
    constructor(private modalService: NgbModal, private router: Router, private alertService: JhiAlertService) {}

    open(component: Component, id?: number | any, fromProfile?: boolean): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }
            if (id) {
                this.candidateSkills = new CandidateSkills();
                this.candidate = new Candidate();
                this.candidateSkills.candidate = this.candidate;
                this.candidateSkills.candidate.id = id;
                setTimeout(() => {
                    this.ngbModalRef = this.candidateSkillModalRef(component, this.candidateSkills, fromProfile);
                    resolve(this.ngbModalRef);
                }, 0);
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.candidateSkillModalRef(component, this.candidateSkills, fromProfile);
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    candidateSkillModalRef(component: Component, candidateSkill: CandidateSkills, fromProfile): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.candidateSkills = candidateSkill;
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

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
