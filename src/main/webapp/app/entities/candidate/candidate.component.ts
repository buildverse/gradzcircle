import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Candidate } from './candidate.model';
import { CandidateService } from './candidate.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-candidate',
    templateUrl: './candidate.component.html'
})
export class CandidateComponent implements OnInit, OnDestroy {
candidates: Candidate[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private candidateService: CandidateService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
         this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.candidateService.search({
                query: this.currentSearch,
                }).subscribe(
                       (res: HttpResponse<Candidate[]>) => this.candidates = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)

                );
            return;
       }
        this.candidateService.query().subscribe(
           (res: HttpResponse<Candidate[]>) => {
                this.candidates = res.body;
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
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInCandidates();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Candidate) {
        return item.id;
    }
    registerChangeInCandidates() {
        this.eventSubscriber = this.eventManager.subscribe('candidateListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
