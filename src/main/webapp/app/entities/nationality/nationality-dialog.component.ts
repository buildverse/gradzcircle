import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Nationality } from './nationality.model';
import { NationalityPopupService } from './nationality-popup.service';
import { NationalityService } from './nationality.service';

@Component({
    selector: 'jhi-nationality-dialog',
    templateUrl: './nationality-dialog.component.html'
})
export class NationalityDialogComponent implements OnInit {

    nationality: Nationality;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private nationalityService: NationalityService,
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
        if (this.nationality.id !== undefined) {
            this.subscribeToSaveResponse(
                this.nationalityService.update(this.nationality));
        } else {
            this.subscribeToSaveResponse(
                this.nationalityService.create(this.nationality));
        }
    }

    private subscribeToSaveResponse(result: Observable<Nationality>) {
        result.subscribe((res: Nationality) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Nationality) {
        this.eventManager.broadcast({ name: 'nationalityListModification', content: 'OK'});
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
    selector: 'jhi-nationality-popup',
    template: ''
})
export class NationalityPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private nationalityPopupService: NationalityPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.nationalityPopupService
                    .open(NationalityDialogComponent as Component, params['id']);
            } else {
                this.nationalityPopupService
                    .open(NationalityDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
