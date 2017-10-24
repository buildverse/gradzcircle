import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Gender } from './gender.model';
import { GenderPopupService } from './gender-popup.service';
import { GenderService } from './gender.service';

@Component({
    selector: 'jhi-gender-delete-dialog',
    templateUrl: './gender-delete-dialog.component.html'
})
export class GenderDeleteDialogComponent {

    gender: Gender;

    constructor(
        private genderService: GenderService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.genderService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'genderListModification',
                content: 'Deleted an gender'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-gender-delete-popup',
    template: ''
})
export class GenderDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private genderPopupService: GenderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.genderPopupService
                .open(GenderDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
