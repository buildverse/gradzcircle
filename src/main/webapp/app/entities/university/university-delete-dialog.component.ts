import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { University } from './university.model';
import { UniversityPopupService } from './university-popup.service';
import { UniversityService } from './university.service';

@Component({
    selector: 'jhi-university-delete-dialog',
    templateUrl: './university-delete-dialog.component.html'
})
export class UniversityDeleteDialogComponent {

    university: University;

    constructor(
        private universityService: UniversityService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.universityService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'universityListModification',
                content: 'Deleted an university'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-university-delete-popup',
    template: ''
})
export class UniversityDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private universityPopupService: UniversityPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.universityPopupService
                .open(UniversityDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
