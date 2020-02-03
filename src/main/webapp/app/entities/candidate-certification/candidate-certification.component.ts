import { Principal } from '../../core/auth/principal.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { AuthoritiesConstants } from '../../shared/authorities.constant';
import { CandidateCertification } from './candidate-certification.model';
import { CandidateCertificationService } from './candidate-certification.service';

import { CANDIDATE_ID, CANDIDATE_CERTIFICATION_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
    selector: 'jhi-candidate-certification',
    templateUrl: './candidate-certification.component.html',
    styleUrls: ['certification.css']
})
export class CandidateCertificationComponent implements OnInit, OnDestroy {
    candidateCertifications: CandidateCertification[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    candidateId: any;
    subscription: Subscription;
    candidateCertificationForDisplay: CandidateCertification[];
    primaryCandidateCertification: CandidateCertification;

    constructor(
        private candidateCertificationService: CandidateCertificationService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private dataservice: DataStorageService,
        private spinnerService: NgxSpinnerService,
        private router: Router
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadCertificationsForCandidate() {
        this.spinnerService.show();
        this.candidateCertificationService.findCertificationsByCandidateId(this.candidateId).subscribe(
            (res: HttpResponse<CandidateCertification[]>) => {
                this.candidateCertifications = res.body;
                // console.log('Certications are '+JSON.stringify(this.candidateCertifications));
                if (this.candidateCertifications && this.candidateCertifications.length <= 0) {
                    this.router.navigate(['./candidate-profile']);
                }
                this.candidateCertificationsOnLoad();
                /* if (this.candidateCertifications && this.candidateCertifications.length > 0) {
                    this.candidateProfileScoreService.changeScore(this.candidateCertifications[0].candidate.profileScore);
                } */
                this.spinnerService.hide();
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        return;
    }

    candidateCertificationsOnLoad() {
        this.candidateCertificationForDisplay = [];
        this.primaryCandidateCertification = undefined;
        if (this.candidateCertifications && this.candidateCertifications.length > 0) {
            this.candidateCertifications.forEach(certification => {
                if (!this.primaryCandidateCertification) {
                    this.primaryCandidateCertification = certification;
                } else {
                    this.candidateCertificationForDisplay.push(certification);
                }
            });
        }
    }

    setPrimaryByUserSelection(event) {
        this.candidateCertificationForDisplay = [];
        this.primaryCandidateCertification = undefined;
        if (this.candidateCertifications && this.candidateCertifications.length > 0) {
            this.candidateCertifications.forEach(certification => {
                if (certification.id === event) {
                    this.primaryCandidateCertification = certification;
                } else {
                    this.candidateCertificationForDisplay.push(certification);
                }
            });
        }
        window.scroll(0, 0);
    }

    setAddRouteParam() {
        this.dataservice.setdata(CANDIDATE_ID, this.candidateId);
        // this.dataservice.setRouteData(this.candidateId);
    }

    setEditDeleteRouteParam(candidateCertificationId) {
        this.dataservice.setdata(CANDIDATE_CERTIFICATION_ID, candidateCertificationId);
        // this.dataservice.setRouteData(candidateCertificationId);
    }

    loadAll() {
        if (this.currentSearch) {
            this.candidateCertificationService
                .searchForAdmin({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<CandidateCertification[]>) => (this.candidateCertifications = res.body),

                    (res: HttpErrorResponse) => this.onError(res.message)
                );

            return;
        }
        this.candidateCertificationService.query().subscribe(
            (res: HttpResponse<CandidateCertification[]>) => {
                this.candidateCertifications = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (account.authorities.indexOf(AuthoritiesConstants.CANDIDATE) > -1) {
                this.candidateId = this.dataservice.getData(CANDIDATE_ID);
                this.currentSearch = this.candidateId;
                this.loadCertificationsForCandidate();
            } else {
                this.loadAll();
            }
            this.registerChangeInCandidateCertifications();
        });
    }

    ngOnDestroy() {
        // console.log('Destroy the component certification');
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CandidateCertification) {
        return item.id;
    }
    registerChangeInCandidateCertifications() {
        if (this.candidateId) {
            this.eventSubscriber = this.eventManager.subscribe('candidateCertificationListModification', response =>
                this.loadCertificationsForCandidate()
            );
        } else {
            this.eventSubscriber = this.eventManager.subscribe('candidateCertificationListModification', response => this.loadAll());
        }
    }

    private onError(error) {
        this.spinnerService.hide();
        this.router.navigate(['/error']);
        this.jhiAlertService.error(error.message, null, null);
    }
}
