import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

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

    private subscribeToSaveResponse(result: Observable<HttpResponse<Employability>>) {
        result.subscribe((res: HttpResponse<Employability>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Employability) {
        this.eventManager.broadcast({ name: 'employabilityListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
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
