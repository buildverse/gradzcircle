import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { CaptureCourse } from './capture-course.model';
import { CaptureCourseService } from './capture-course.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-capture-course',
    templateUrl: './capture-course.component.html'
})
export class CaptureCourseComponent implements OnInit, OnDestroy {
captureCourses: CaptureCourse[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private captureCourseService: CaptureCourseService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch.length>0) {
            this.captureCourseService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.captureCourses = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.captureCourseService.query().subscribe(
            (res: ResponseWrapper) => {
                this.captureCourses = res.json;
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
        this.registerChangeInCaptureCourses();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CaptureCourse) {
        return item.id;
    }
    registerChangeInCaptureCourses() {
        this.eventSubscriber = this.eventManager.subscribe('captureCourseListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
