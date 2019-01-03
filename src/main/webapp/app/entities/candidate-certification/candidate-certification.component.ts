import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager,  JhiAlertService } from 'ng-jhipster';
import { AuthoritiesConstants } from '../../shared/authorities.constant';
import { CandidateCertification } from './candidate-certification.model';
import { CandidateCertificationService } from './candidate-certification.service';
import { Principal, DataService } from '../../shared';

import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

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

    constructor(
        private candidateCertificationService: CandidateCertificationService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private dataservice: DataService
    ) {
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    /*To be removed once undertsand Elastic */
    loadCertificationsForCandidate() {
        this.candidateCertificationService.findCertificationsByCandidateId(this.candidateId).subscribe(
                (res: HttpResponse<CandidateCertification[]>) => this.candidateCertifications = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
        );
        return;

    }
  
    setAddRouteParam() {
      this.dataservice.setRouteData(this.candidateId);  
    }
  
    setEditDeleteRouteParam(candidateCertificationId) {
      this.dataservice.setRouteData(candidateCertificationId);
    }
  
  
    loadAll() {
        if (this.currentSearch) {
            this.candidateCertificationService.searchForAdmin({
                query: this.currentSearch,
                }).subscribe(
                        (res: HttpResponse<CandidateCertification[]>) => this.candidateCertifications = res.body,
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

        this.principal.identity().then((account) => {
            this.currentAccount = account;
            if (account.authorities.indexOf(AuthoritiesConstants.CANDIDATE) > -1 ) {
                this.candidateId = this.activatedRoute.snapshot.parent.data['candidate'].body.id;
                this.currentSearch = this.candidateId;
                this.loadCertificationsForCandidate();
            } else {
                this.loadAll();
            }
            this.registerChangeInCandidateCertifications();
        });

    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CandidateCertification) {
        return item.id;
    }
    registerChangeInCandidateCertifications() {
        if (this.candidateId) {
            this.eventSubscriber = this.eventManager.subscribe('candidateCertificationListModification', (response) => this.loadCertificationsForCandidate());
        } else {
            this.eventSubscriber = this.eventManager.subscribe('candidateCertificationListModification', (response) => this.loadAll());
      }
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
