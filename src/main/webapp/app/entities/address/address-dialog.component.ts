import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Address } from './address.model';
import { AddressPopupService } from './address-popup.service';
import { AddressService } from './address.service';
import { Candidate, CandidateService } from '../candidate';
import { Country, CountryService } from '../country';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-address-dialog',
    templateUrl: './address-dialog.component.html'
})
export class AddressDialogComponent implements OnInit {

    address: Address;
    isSaving: boolean;

    candidates: Candidate[];

    countries: Country[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private addressService: AddressService,
        private candidateService: CandidateService,
        private countryService: CountryService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.candidateService.query()
            .subscribe((res: ResponseWrapper) => { this.candidates = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.countryService.query()
            .subscribe((res: ResponseWrapper) => { this.countries = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.address.id !== undefined) {
            this.subscribeToSaveResponse(
                this.addressService.update(this.address));
        } else {
            this.subscribeToSaveResponse(
                this.addressService.create(this.address));
        }
    }

    private subscribeToSaveResponse(result: Observable<Address>) {
        result.subscribe((res: Address) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Address) {
        this.eventManager.broadcast({ name: 'addressListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCandidateById(index: number, item: Candidate) {
        return item.id;
    }

    trackCountryById(index: number, item: Country) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-address-popup',
    template: ''
})
export class AddressPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private addressPopupService: AddressPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.addressPopupService
                    .open(AddressDialogComponent as Component, params['id']);
            } else {
                this.addressPopupService
                    .open(AddressDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
