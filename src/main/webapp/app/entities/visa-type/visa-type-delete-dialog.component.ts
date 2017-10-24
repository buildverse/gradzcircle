import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { VisaType } from './visa-type.model';
import { VisaTypePopupService } from './visa-type-popup.service';
import { VisaTypeService } from './visa-type.service';

@Component({
    selector: 'jhi-visa-type-delete-dialog',
    templateUrl: './visa-type-delete-dialog.component.html'
})
export class VisaTypeDeleteDialogComponent {

    visaType: VisaType;

    constructor(
        private visaTypeService: VisaTypeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.visaTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'visaTypeListModification',
                content: 'Deleted an visaType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-visa-type-delete-popup',
    template: ''
})
export class VisaTypeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private visaTypePopupService: VisaTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.visaTypePopupService
                .open(VisaTypeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
