import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { CaptureQualification } from './capture-qualification.model';
import { CaptureQualificationService } from './capture-qualification.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-capture-qualification',
    templateUrl: './capture-qualification.component.html'
})
export class CaptureQualificationComponent implements OnInit, OnDestroy {
captureQualifications: CaptureQualification[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private captureQualificationService: CaptureQualificationService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.captureQualificationService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.captureQualifications = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.captureQualificationService.query().subscribe(
            (res: ResponseWrapper) => {
                this.captureQualifications = res.json;
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
        this.registerChangeInCaptureQualifications();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CaptureQualification) {
        return item.id;
    }
    registerChangeInCaptureQualifications() {
        this.eventSubscriber = this.eventManager.subscribe('captureQualificationListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
