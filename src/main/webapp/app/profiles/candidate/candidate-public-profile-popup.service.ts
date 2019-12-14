import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CandidatePublicProfile } from '../../entities/candidate/candidate-public-profile.model';
import { CandidateService } from '../../entities/candidate/candidate.service';
import { HttpResponse } from '@angular/common/http';

@Injectable()
export class CandidatePublicProfilePopupService {
    private ngbModalRef: NgbModalRef;
    constructor(private modalService: NgbModal, private router: Router, private candidateService: CandidateService) {
        this.ngbModalRef = null;
    }

    open(
        component: Component,
        id?: number | any,
        jobId?: number,
        corporateId?: number,
        fromLinkedCandidate?: boolean
    ): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.candidateService
                    .getCandidatePublicProfile(id, jobId, corporateId)
                    .subscribe((candidateResponse: HttpResponse<CandidatePublicProfile>) => {
                        const candidate: CandidatePublicProfile = candidateResponse.body;
                        this.ngbModalRef = this.candidateModalRef(component, candidate, jobId, corporateId, fromLinkedCandidate);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.candidateModalRef(component, new CandidatePublicProfile());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    candidateModalRef(
        component: Component,
        candidate: CandidatePublicProfile,
        jobId?: number,
        corporateId?: number,
        fromLinkedCandidate?: boolean
    ): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.candidate = candidate;
        modalRef.componentInstance.jobId = jobId;
        modalRef.componentInstance.corporateId = corporateId;
        modalRef.componentInstance.fromLinkedCandidate = fromLinkedCandidate;
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
