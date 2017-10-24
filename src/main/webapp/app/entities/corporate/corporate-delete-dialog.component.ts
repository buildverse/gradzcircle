import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Corporate } from './corporate.model';
import { CorporatePopupService } from './corporate-popup.service';
import { CorporateService } from './corporate.service';

@Component({
    selector: 'jhi-corporate-delete-dialog',
    templateUrl: './corporate-delete-dialog.component.html'
})
export class CorporateDeleteDialogComponent {

    corporate: Corporate;

    constructor(
        private corporateService: CorporateService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.corporateService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'corporateListModification',
                content: 'Deleted an corporate'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-corporate-delete-popup',
    template: ''
})
export class CorporateDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private corporatePopupService: CorporatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.corporatePopupService
                .open(CorporateDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
