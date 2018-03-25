import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { Candidate } from '../candidate/candidate.model';
import { CandidateLanguageProficiency } from './candidate-language-proficiency.model';
import { CandidateLanguageProficiencyService } from './candidate-language-proficiency.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { AuthoritiesConstants } from '../../shared/authorities.constant';

@Component({
    selector: 'jhi-candidate-language-proficiency',
    templateUrl: './candidate-language-proficiency.component.html'
})
export class CandidateLanguageProficiencyComponent implements OnInit, OnDestroy {
    candidateLanguageProficiencies: CandidateLanguageProficiency[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    candidateId: any;


    constructor(
        private candidateLanguageProficiencyService: CandidateLanguageProficiencyService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.candidateLanguageProficiencyService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.candidateLanguageProficiencies = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.candidateLanguageProficiencyService.query().subscribe(
            (res: ResponseWrapper) => {
                this.candidateLanguageProficiencies = res.json;
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

    loadCandidateLanguages(){
        this.candidateLanguageProficiencyService.findByCandidateId(this.candidateId)
            .subscribe(
                (response: ResponseWrapper)=> {
                    this.candidateLanguageProficiencies = response.json;
                },
                (response: ResponseWrapper) => {
                    this.onError(response.json);
                    
                }
            );
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
            if(account.authorities.indexOf(AuthoritiesConstants.CANDIDATE)>-1){
                this.candidateId = this.activatedRoute.snapshot.parent.data['candidate'].id;
                this.currentSearch = this.candidateId;
                this.loadCandidateLanguages();
            }
        });
        this.registerChangeInCandidateLanguageProficiencies();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CandidateLanguageProficiency) {
        return item.id;
    }
    registerChangeInCandidateLanguageProficiencies() {
        this.eventSubscriber = this.eventManager.subscribe('candidateLanguageProficiencyListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
