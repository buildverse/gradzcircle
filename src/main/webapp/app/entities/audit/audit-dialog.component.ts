import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Audit } from './audit.model';
import { AuditPopupService } from './audit-popup.service';
import { AuditService } from './audit.service';

@Component({
    selector: 'jhi-audit-dialog',
    templateUrl: './audit-dialog.component.html'
})
export class AuditDialogComponent implements OnInit {

    audit: Audit;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private auditService: AuditService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.audit.id !== undefined) {
            this.subscribeToSaveResponse(
                this.auditService.update(this.audit));
        } else {
            this.subscribeToSaveResponse(
                this.auditService.create(this.audit));
        }
    }

    private subscribeToSaveResponse(result: Observable<Audit>) {
        result.subscribe((res: Audit) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Audit) {
        this.eventManager.broadcast({ name: 'auditListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-audit-popup',
    template: ''
})
export class AuditPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private auditPopupService: AuditPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.auditPopupService
                    .open(AuditDialogComponent as Component, params['id']);
            } else {
                this.auditPopupService
                    .open(AuditDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
