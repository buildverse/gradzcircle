import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Employability } from './employability.model';
import { EmployabilityPopupService } from './employability-popup.service';
import { EmployabilityService } from './employability.service';

@Component({
    selector: 'jhi-employability-delete-dialog',
    templateUrl: './employability-delete-dialog.component.html'
})
export class EmployabilityDeleteDialogComponent {

    employability: Employability;

    constructor(
        private employabilityService: EmployabilityService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.employabilityService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'employabilityListModification',
                content: 'Deleted an employability'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-employability-delete-popup',
    template: ''
})
export class EmployabilityDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private employabilityPopupService: EmployabilityPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.employabilityPopupService
                .open(EmployabilityDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
