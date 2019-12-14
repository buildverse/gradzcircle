import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { FilterCategory } from './filter-category.model';
import { FilterCategoryService } from './filter-category.service';

@Component({
    selector: 'jhi-filter-category-detail',
    templateUrl: './filter-category-detail.component.html'
})
export class FilterCategoryDetailComponent implements OnInit, OnDestroy {
    filterCategory: FilterCategory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private filterCategoryService: FilterCategoryService,
        private route: ActivatedRoute
    ) {}

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerChangeInFilterCategories();
    }

    load(id) {
        this.filterCategoryService.find(id).subscribe((filterCategoryResponse: HttpResponse<FilterCategory>) => {
            this.filterCategory = filterCategoryResponse.body;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInFilterCategories() {
        this.eventSubscriber = this.eventManager.subscribe('filterCategoryListModification', response => this.load(this.filterCategory.id));
    }
}
