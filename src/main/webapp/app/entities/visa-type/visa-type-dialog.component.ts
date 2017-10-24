import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { VisaType } from './visa-type.model';
import { VisaTypePopupService } from './visa-type-popup.service';
import { VisaTypeService } from './visa-type.service';
import { Country, CountryService } from '../country';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-visa-type-dialog',
    templateUrl: './visa-type-dialog.component.html'
})
export class VisaTypeDialogComponent implements OnInit {

    visaType: VisaType;
    isSaving: boolean;

    countries: Country[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private visaTypeService: VisaTypeService,
        private countryService: CountryService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.countryService.query()
            .subscribe((res: ResponseWrapper) => { this.countries = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.visaType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.visaTypeService.update(this.visaType));
        } else {
            this.subscribeToSaveResponse(
                this.visaTypeService.create(this.visaType));
        }
    }

    private subscribeToSaveResponse(result: Observable<VisaType>) {
        result.subscribe((res: VisaType) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: VisaType) {
        this.eventManager.broadcast({ name: 'visaTypeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCountryById(index: number, item: Country) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-visa-type-popup',
    template: ''
})
export class VisaTypePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private visaTypePopupService: VisaTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.visaTypePopupService
                    .open(VisaTypeDialogComponent as Component, params['id']);
            } else {
                this.visaTypePopupService
                    .open(VisaTypeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
