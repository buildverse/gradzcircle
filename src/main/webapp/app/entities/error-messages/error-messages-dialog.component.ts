import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ErrorMessages } from './error-messages.model';
import { ErrorMessagesPopupService } from './error-messages-popup.service';
import { ErrorMessagesService } from './error-messages.service';

@Component({
    selector: 'jhi-error-messages-dialog',
    templateUrl: './error-messages-dialog.component.html'
})
export class ErrorMessagesDialogComponent implements OnInit {
    errorMessages: ErrorMessages;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private errorMessagesService: ErrorMessagesService,
        private eventManager: JhiEventManager,
        private router: Router
    ) {}

    ngOnInit() {
        this.isSaving = false;
    }

    clearRoute() {
        this.router.navigate(['/admin', { outlets: { popup: null } }]);
    }

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.errorMessages.id !== undefined) {
            this.subscribeToSaveResponse(this.errorMessagesService.update(this.errorMessages));
        } else {
            this.subscribeToSaveResponse(this.errorMessagesService.create(this.errorMessages));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ErrorMessages>>) {
        result.subscribe(
            (res: HttpResponse<ErrorMessages>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess(result: ErrorMessages) {
        this.eventManager.broadcast({ name: 'errorMessagesListModification', content: 'OK' });
        this.isSaving = false;
        this.clearRoute();
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-error-messages-popup',
    template: ''
})
export class ErrorMessagesPopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private errorMessagesPopupService: ErrorMessagesPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.errorMessagesPopupService.open(ErrorMessagesDialogComponent as Component, params['id']);
            } else {
                this.errorMessagesPopupService.open(ErrorMessagesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
