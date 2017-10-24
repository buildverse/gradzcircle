import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { CandidateProject } from './candidate-project.model';
import { CandidateProjectService } from './candidate-project.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-candidate-project',
    templateUrl: './candidate-project.component.html'
})
export class CandidateProjectComponent implements OnInit, OnDestroy {
candidateProjects: CandidateProject[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private candidateProjectService: CandidateProjectService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.candidateProjectService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.candidateProjects = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.candidateProjectService.query().subscribe(
            (res: ResponseWrapper) => {
                this.candidateProjects = res.json;
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
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInCandidateProjects();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CandidateProject) {
        return item.id;
    }
    registerChangeInCandidateProjects() {
        this.eventSubscriber = this.eventManager.subscribe('candidateProjectListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
