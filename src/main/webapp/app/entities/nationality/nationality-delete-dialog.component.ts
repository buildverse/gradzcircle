import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Nationality } from './nationality.model';
import { NationalityPopupService } from './nationality-popup.service';
import { NationalityService } from './nationality.service';

@Component({
    selector: 'jhi-nationality-delete-dialog',
    templateUrl: './nationality-delete-dialog.component.html'
})
export class NationalityDeleteDialogComponent {
    nationality: Nationality;

    constructor(
        private nationalityService: NationalityService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.nationalityService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'nationalityListModification',
                content: 'Deleted an nationality'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-nationality-delete-popup',
    template: ''
})
export class NationalityDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private nationalityPopupService: NationalityPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.nationalityPopupService.open(NationalityDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
