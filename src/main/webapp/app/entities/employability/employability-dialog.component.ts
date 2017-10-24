import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Employability } from './employability.model';
import { EmployabilityPopupService } from './employability-popup.service';
import { EmployabilityService } from './employability.service';

@Component({
    selector: 'jhi-employability-dialog',
    templateUrl: './employability-dialog.component.html'
})
export class EmployabilityDialogComponent implements OnInit {

    employability: Employability;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private employabilityService: EmployabilityService,
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
        if (this.employability.id !== undefined) {
            this.subscribeToSaveResponse(
                this.employabilityService.update(this.employability));
        } else {
            this.subscribeToSaveResponse(
                this.employabilityService.create(this.employability));
        }
    }

    private subscribeToSaveResponse(result: Observable<Employability>) {
        result.subscribe((res: Employability) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Employability) {
        this.eventManager.broadcast({ name: 'employabilityListModification', content: 'OK'});
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
    selector: 'jhi-employability-popup',
    template: ''
})
export class EmployabilityPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private employabilityPopupService: EmployabilityPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.employabilityPopupService
                    .open(EmployabilityDialogComponent as Component, params['id']);
            } else {
                this.employabilityPopupService
                    .open(EmployabilityDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
