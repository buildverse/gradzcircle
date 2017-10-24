import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { AuthoritiesConstants } from '../../shared/authorities.constant';
import { CandidateCertification } from './candidate-certification.model';
import { CandidateCertificationService } from './candidate-certification.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-candidate-certification',
    templateUrl: './candidate-certification.component.html'
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
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    /*To be removed once undertsand Elastic */
    loadCertificationsForCandidate() {
        this.candidateCertificationService.findCertificationsByCandidateId(this.candidateId).subscribe(
            (res: ResponseWrapper) => this.candidateCertifications = res.json,
            (res: ResponseWrapper) => this.onError(res.json)
        );
        return;

    }
    loadAll() {
        if (this.currentSearch) {
            this.candidateCertificationService.searchForAdmin({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.candidateCertifications = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.candidateCertificationService.query().subscribe(
            (res: ResponseWrapper) => {
                this.candidateCertifications = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
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
        console.log("In inti");
        this.principal.identity().then((account) => {
            this.currentAccount = account;
            console.log("Account is "+ this.currentAccount);
            if(account.authorities.indexOf(AuthoritiesConstants.CANDIDATE)>-1){
                this.candidateId = this.activatedRoute.snapshot.parent.data['candidate'].id;
                this.currentSearch = this.candidateId;
                this.loadCertificationsForCandidate();
            } else {
                this.loadAll();
            }
            this.registerChangeInCandidateCertifications();
        });
        console.log("exit inti");
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
        }
        else
            this.eventSubscriber = this.eventManager.subscribe('candidateCertificationListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
