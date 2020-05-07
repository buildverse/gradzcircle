import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Filter } from './filter.model';
import { FilterPopupService } from './filter-popup.service';
import { FilterService } from './filter.service';

@Component({
    selector: 'jhi-filter-dialog',
    templateUrl: './filter-dialog.component.html'
})
export class FilterDialogComponent implements OnInit {
    filter: Filter;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private filterService: FilterService,
        private eventManager: JhiEventManager,
        private router: Router
    ) {}

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    clearRoute() {
        this.router.navigate(['/admin', { outlets: { popup: null } }]);
    }

    save() {
        this.isSaving = true;
        if (this.filter.id !== undefined) {
            this.subscribeToSaveResponse(this.filterService.update(this.filter));
        } else {
            this.subscribeToSaveResponse(this.filterService.create(this.filter));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Filter>>) {
        result.subscribe((res: HttpResponse<Filter>) => this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Filter) {
        this.eventManager.broadcast({ name: 'filterListModification', content: 'OK' });
        this.isSaving = false;
        this.clearRoute();
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-filter-popup',
    template: ''
})
export class FilterPopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private filterPopupService: FilterPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.filterPopupService.open(FilterDialogComponent as Component, params['id']);
            } else {
                this.filterPopupService.open(FilterDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
