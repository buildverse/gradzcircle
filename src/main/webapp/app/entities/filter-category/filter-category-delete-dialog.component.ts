import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { FilterCategory } from './filter-category.model';
import { FilterCategoryPopupService } from './filter-category-popup.service';
import { FilterCategoryService } from './filter-category.service';

@Component({
    selector: 'jhi-filter-category-delete-dialog',
    templateUrl: './filter-category-delete-dialog.component.html'
})
export class FilterCategoryDeleteDialogComponent {

    filterCategory: FilterCategory;

    constructor(
        private filterCategoryService: FilterCategoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.filterCategoryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'filterCategoryListModification',
                content: 'Deleted an filterCategory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-filter-category-delete-popup',
    template: ''
})
export class FilterCategoryDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private filterCategoryPopupService: FilterCategoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.filterCategoryPopupService
                .open(FilterCategoryDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
