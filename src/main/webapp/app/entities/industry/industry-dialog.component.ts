import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Industry } from './industry.model';
import { IndustryPopupService } from './industry-popup.service';
import { IndustryService } from './industry.service';

@Component({
    selector: 'jhi-industry-dialog',
    templateUrl: './industry-dialog.component.html'
})
export class IndustryDialogComponent implements OnInit {

    industry: Industry;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private industryService: IndustryService,
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
        if (this.industry.id !== undefined) {
            this.subscribeToSaveResponse(
                this.industryService.update(this.industry));
        } else {
            this.subscribeToSaveResponse(
                this.industryService.create(this.industry));
        }
    }

    private subscribeToSaveResponse(result: Observable<Industry>) {
        result.subscribe((res: Industry) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Industry) {
        this.eventManager.broadcast({ name: 'industryListModification', content: 'OK'});
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
    selector: 'jhi-industry-popup',
    template: ''
})
export class IndustryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private industryPopupService: IndustryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.industryPopupService
                    .open(IndustryDialogComponent as Component, params['id']);
            } else {
                this.industryPopupService
                    .open(IndustryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
