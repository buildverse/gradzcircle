import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

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

    private subscribeToSaveResponse(result: Observable<HttpResponse<Industry>>) {
        result.subscribe((res: HttpResponse<Industry>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Industry) {
        this.eventManager.broadcast({ name: 'industryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
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
