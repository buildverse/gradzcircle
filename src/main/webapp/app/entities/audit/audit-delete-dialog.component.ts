import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Audit } from './audit.model';
import { AuditPopupService } from './audit-popup.service';
import { AuditService } from './audit.service';

@Component({
    selector: 'jhi-audit-delete-dialog',
    templateUrl: './audit-delete-dialog.component.html'
})
export class AuditDeleteDialogComponent {

    audit: Audit;

    constructor(
        private auditService: AuditService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.auditService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'auditListModification',
                content: 'Deleted an audit'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-audit-delete-popup',
    template: ''
})
export class AuditDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private auditPopupService: AuditPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.auditPopupService
                .open(AuditDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
