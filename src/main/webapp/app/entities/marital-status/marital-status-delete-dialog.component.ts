import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MaritalStatus } from './marital-status.model';
import { MaritalStatusPopupService } from './marital-status-popup.service';
import { MaritalStatusService } from './marital-status.service';

@Component({
    selector: 'jhi-marital-status-delete-dialog',
    templateUrl: './marital-status-delete-dialog.component.html'
})
export class MaritalStatusDeleteDialogComponent {
    maritalStatus: MaritalStatus;

    constructor(
        private maritalStatusService: MaritalStatusService,
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
        this.maritalStatusService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'maritalStatusListModification',
                content: 'Deleted an maritalStatus'
            });
            this.clearRoute();
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-marital-status-delete-popup',
    template: ''
})
export class MaritalStatusDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private maritalStatusPopupService: MaritalStatusPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.maritalStatusPopupService.open(MaritalStatusDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
