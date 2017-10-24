import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Gender } from './gender.model';
import { GenderPopupService } from './gender-popup.service';
import { GenderService } from './gender.service';

@Component({
    selector: 'jhi-gender-dialog',
    templateUrl: './gender-dialog.component.html'
})
export class GenderDialogComponent implements OnInit {

    gender: Gender;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private genderService: GenderService,
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
        if (this.gender.id !== undefined) {
            this.subscribeToSaveResponse(
                this.genderService.update(this.gender));
        } else {
            this.subscribeToSaveResponse(
                this.genderService.create(this.gender));
        }
    }

    private subscribeToSaveResponse(result: Observable<Gender>) {
        result.subscribe((res: Gender) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Gender) {
        this.eventManager.broadcast({ name: 'genderListModification', content: 'OK'});
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
    selector: 'jhi-gender-popup',
    template: ''
})
export class GenderPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private genderPopupService: GenderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.genderPopupService
                    .open(GenderDialogComponent as Component, params['id']);
            } else {
                this.genderPopupService
                    .open(GenderDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
