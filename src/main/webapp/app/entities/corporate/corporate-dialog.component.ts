import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute,Router } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Corporate } from './corporate.model';
import { CorporatePopupService } from './corporate-popup.service';
import { CorporateService } from './corporate.service';
import { Country, CountryService } from '../country';
import { Industry, IndustryService } from '../industry';
import { User, UserService } from '../../shared';
import { ResponseWrapper ,EditorProperties} from '../../shared';


@Component({
    selector: 'jhi-corporate-dialog',
    templateUrl: './corporate-dialog.component.html'
})
export class CorporateDialogComponent implements OnInit {

    corporate: Corporate;
    isSaving: boolean;
    options: Object;
    countries: Country[];
    industries: Industry[];
    users: User[];
    establishedSinceDp: any;


    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private corporateService: CorporateService,
        private countryService: CountryService,
        private industryService: IndustryService,
        private eventManager: JhiEventManager,
        private userService: UserService,

    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.options = new EditorProperties().options;
        this.countryService.query()
            .subscribe((res: ResponseWrapper) => { this.countries = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.industryService.query()
            .subscribe((res: ResponseWrapper) => { this.industries = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.corporate.id !== undefined) {
            this.subscribeToSaveResponse(
                this.corporateService.update(this.corporate));
        } else {
            this.subscribeToSaveResponse(
                this.corporateService.create(this.corporate));
        }
    }

    private subscribeToSaveResponse(result: Observable<Corporate>) {
        result.subscribe((res: Corporate) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Corporate) {
        this.eventManager.broadcast({ name: 'corporateListModification', content: 'OK'});
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

    trackIndustryById(index: number, item: Industry) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-corporate-popup',
    template: ''
})
export class CorporatePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private corporatePopupService: CorporatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.corporatePopupService
                    .open(CorporateDialogComponent as Component, params['id']);
            } else {
                this.corporatePopupService
                    .open(CorporateDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
