import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

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
        private nationalityService: NationalityService,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.nationality.id !== undefined) {
            this.subscribeToSaveResponse(this.nationalityService.update(this.nationality));
        } else {
            this.subscribeToSaveResponse(this.nationalityService.create(this.nationality));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Nationality>>) {
        result.subscribe((res: HttpResponse<Nationality>) => this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Nationality) {
        this.eventManager.broadcast({ name: 'nationalityListModification', content: 'OK' });
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-nationality-popup',
    template: ''
})
export class NationalityPopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private nationalityPopupService: NationalityPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.nationalityPopupService.open(NationalityDialogComponent as Component, params['id']);
            } else {
                this.nationalityPopupService.open(NationalityDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
