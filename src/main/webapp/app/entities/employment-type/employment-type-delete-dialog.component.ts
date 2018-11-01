import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { EmploymentType } from './employment-type.model';
import { EmploymentTypePopupService } from './employment-type-popup.service';
import { EmploymentTypeService } from './employment-type.service';

@Component({
    selector: 'jhi-employment-type-delete-dialog',
    templateUrl: './employment-type-delete-dialog.component.html'
})
export class EmploymentTypeDeleteDialogComponent {

    employmentType: EmploymentType;

    constructor(
        private employmentTypeService: EmploymentTypeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.employmentTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'employmentTypeListModification',
                content: 'Deleted an employmentType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-employment-type-delete-popup',
    template: ''
})
export class EmploymentTypeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private employmentTypePopupService: EmploymentTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.employmentTypePopupService
                .open(EmploymentTypeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
