import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { Candidate } from '../candidate/candidate.model';
import { CandidateLanguageProficiency } from './candidate-language-proficiency.model';
import { CandidateLanguageProficiencyService } from './candidate-language-proficiency.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { AuthoritiesConstants } from '../../shared/authorities.constant';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

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
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch.length>0) {
          
        console.log('In search');
            this.candidateLanguageProficiencyService.search({
                query: this.currentSearch,
                }).subscribe(
                     (res: HttpResponse<CandidateLanguageProficiency[]>) => this.candidateLanguageProficiencies = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.candidateLanguageProficiencyService.query().subscribe(
             (res: HttpResponse<CandidateLanguageProficiency[]>) => {
                this.candidateLanguageProficiencies = res.body;
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

    loadCandidateLanguages(){
        this.candidateLanguageProficiencyService.findByCandidateId(this.candidateId)
            .subscribe(
                (res: HttpResponse<CandidateLanguageProficiency[]>) => this.candidateLanguageProficiencies = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
            if(account.authorities.indexOf(AuthoritiesConstants.CANDIDATE)>-1){
                this.candidateId = this.activatedRoute.snapshot.parent.data['candidate'].id;
                this.currentSearch = this.candidateId;
                this.loadCandidateLanguages();
            } else {
              this.loadAll();
            }
          this.registerChangeInCandidateLanguageProficiencies();
        });
       // this.registerChangeInCandidateLanguageProficiencies();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CandidateLanguageProficiency) {
        return item.id;
    }
    registerChangeInCandidateLanguageProficiencies() {
      console.log('CANDIDATE ID IS '+JSON.stringify(this.candidateId));
       if (this.candidateId) {

            this.eventSubscriber = this.eventManager.subscribe('candidateLanguageProficiencyListModification', (response) => this.loadCandidateLanguages());
            this.eventSubscriber = this.eventManager.subscribe('candidateLanguageProficiencyListModification', (response) => this.loadCandidateLanguages());
        }
        else {
            this.eventSubscriber = this.eventManager.subscribe('candidateLanguageProficiencyListModification', (response) => this.loadAll());
      }

     //   this.eventSubscriber = this.eventManager.subscribe('candidateLanguageProficiencyListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
