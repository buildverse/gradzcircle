import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Filter } from './filter.model';
import { FilterPopupService } from './filter-popup.service';
import { FilterService } from './filter.service';

@Component({
    selector: 'jhi-filter-delete-dialog',
    templateUrl: './filter-delete-dialog.component.html'
})
export class FilterDeleteDialogComponent {
    filter: Filter;

    constructor(
        private filterService: FilterService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router
    ) {}

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    clearRoute() {
        this.router.navigate(['/admin', { outlets: { popup: null } }]);
    }

    confirmDelete(id: number) {
        this.filterService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'filterListModification',
                content: 'Deleted an filter'
            });
            this.clearRoute();
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-filter-delete-popup',
    template: ''
})
export class FilterDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private filterPopupService: FilterPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.filterPopupService.open(FilterDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
