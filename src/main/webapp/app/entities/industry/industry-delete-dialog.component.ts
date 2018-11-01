import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Industry } from './industry.model';
import { IndustryPopupService } from './industry-popup.service';
import { IndustryService } from './industry.service';

@Component({
    selector: 'jhi-industry-delete-dialog',
    templateUrl: './industry-delete-dialog.component.html'
})
export class IndustryDeleteDialogComponent {

    industry: Industry;

    constructor(
        private industryService: IndustryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.industryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'industryListModification',
                content: 'Deleted an industry'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-industry-delete-popup',
    template: ''
})
export class IndustryDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private industryPopupService: IndustryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.industryPopupService
                .open(IndustryDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
