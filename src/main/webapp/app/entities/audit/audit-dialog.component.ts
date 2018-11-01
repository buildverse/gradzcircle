import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

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

    private subscribeToSaveResponse(result: Observable<HttpResponse<Audit>>) {
        result.subscribe((res: HttpResponse<Audit>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Audit) {
        this.eventManager.broadcast({ name: 'auditListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
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
