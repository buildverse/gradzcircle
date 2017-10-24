import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { CandidateEducation } from './candidate-education.model';
import { CandidateEducationService } from './candidate-education.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { AuthoritiesConstants } from '../../shared/authorities.constant';


@Component({
    selector: 'jhi-candidate-education',
    templateUrl: './candidate-education.component.html'
})
export class CandidateEducationComponent implements OnInit, OnDestroy {
    candidateEducations: CandidateEducation[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    candidateId: any;
    subscription: Subscription;


    constructor(
        private candidateEducationService: CandidateEducationService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.candidateEducationService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.candidateEducations = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
        } else {
            this.candidateEducationService.query().subscribe(
                (res: ResponseWrapper) => {
                    this.candidateEducations = res.json;
                    this.currentSearch = '';
                },
                (res: ResponseWrapper) => this.onError(res.json)
            );
        }
    }
    /*To be removed once undertsand Elastic */
    loadEducationForCandidate() {

        this.candidateEducationService.findEducationByCandidateId(this.candidateId).subscribe(
           (res: ResponseWrapper) => {
                    this.candidateEducations = res.json;
                },
                (res: ResponseWrapper) => this.onError(res.json)
        );
        return;
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
            if(account.authorities.indexOf(AuthoritiesConstants.CANDIDATE)>-1){
                this.candidateId = this.activatedRoute.snapshot.parent.data['candidate'].id;
                this.currentSearch = this.candidateId;
                this.loadEducationForCandidate();
            } else {
                this.loadAll();
            }
            this.registerChangeInCandidateEducations();
        });


    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CandidateEducation) {
        return item.id;
    }
    registerChangeInCandidateEducations() {

        if (this.candidateId) {

            this.eventSubscriber = this.eventManager.subscribe('candidateEducationListModification', (response) => this.loadEducationForCandidate());
            this.eventSubscriber = this.eventManager.subscribe('candidateProjectListModification', (response) => this.loadEducationForCandidate());
        }
        else
            this.eventSubscriber = this.eventManager.subscribe('candidateEducationListModification', (response) => this.loadAll());

    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
