import { Principal } from '../../core/auth/principal.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { FilterCategory } from './filter-category.model';
import { FilterCategoryService } from './filter-category.service';

@Component({
    selector: 'jhi-filter-category',
    templateUrl: './filter-category.component.html'
})
export class FilterCategoryComponent implements OnInit, OnDestroy {
    filterCategories: FilterCategory[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private filterCategoryService: FilterCategoryService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.filterCategoryService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<FilterCategory[]>) => (this.filterCategories = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.filterCategoryService.query().subscribe(
            (res: HttpResponse<FilterCategory[]>) => {
                this.filterCategories = res.body;
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
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInFilterCategories();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: FilterCategory) {
        return item.id;
    }
    registerChangeInFilterCategories() {
        this.eventSubscriber = this.eventManager.subscribe('filterCategoryListModification', response => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
