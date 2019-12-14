import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { FilterCategory } from './filter-category.model';
import { FilterCategoryPopupService } from './filter-category-popup.service';
import { FilterCategoryService } from './filter-category.service';

@Component({
    selector: 'jhi-filter-category-dialog',
    templateUrl: './filter-category-dialog.component.html'
})
export class FilterCategoryDialogComponent implements OnInit {
    filterCategory: FilterCategory;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private filterCategoryService: FilterCategoryService,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.filterCategory.id !== undefined) {
            this.subscribeToSaveResponse(this.filterCategoryService.update(this.filterCategory));
        } else {
            this.subscribeToSaveResponse(this.filterCategoryService.create(this.filterCategory));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<FilterCategory>>) {
        result.subscribe(
            (res: HttpResponse<FilterCategory>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess(result: FilterCategory) {
        this.eventManager.broadcast({ name: 'filterCategoryListModification', content: 'OK' });
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-filter-category-popup',
    template: ''
})
export class FilterCategoryPopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private filterCategoryPopupService: FilterCategoryPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.filterCategoryPopupService.open(FilterCategoryDialogComponent as Component, params['id']);
            } else {
                this.filterCategoryPopupService.open(FilterCategoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
