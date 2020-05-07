import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MaritalStatus } from './marital-status.model';
import { MaritalStatusPopupService } from './marital-status-popup.service';
import { MaritalStatusService } from './marital-status.service';

@Component({
    selector: 'jhi-marital-status-dialog',
    templateUrl: './marital-status-dialog.component.html'
})
export class MaritalStatusDialogComponent implements OnInit {
    maritalStatus: MaritalStatus;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private maritalStatusService: MaritalStatusService,
        private eventManager: JhiEventManager,
        private router: Router
    ) {}

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    clearRoute() {
        this.router.navigate(['/admin', { outlets: { popup: null } }]);
    }

    save() {
        this.isSaving = true;
        if (this.maritalStatus.id !== undefined) {
            this.subscribeToSaveResponse(this.maritalStatusService.update(this.maritalStatus));
        } else {
            this.subscribeToSaveResponse(this.maritalStatusService.create(this.maritalStatus));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<MaritalStatus>>) {
        result.subscribe(
            (res: HttpResponse<MaritalStatus>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess(result: MaritalStatus) {
        this.eventManager.broadcast({ name: 'maritalStatusListModification', content: 'OK' });
        this.isSaving = false;
        this.clearRoute();
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-marital-status-popup',
    template: ''
})
export class MaritalStatusPopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private maritalStatusPopupService: MaritalStatusPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.maritalStatusPopupService.open(MaritalStatusDialogComponent as Component, params['id']);
            } else {
                this.maritalStatusPopupService.open(MaritalStatusDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
