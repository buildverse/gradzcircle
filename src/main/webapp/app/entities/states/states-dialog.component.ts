import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { States } from './states.model';
import { StatesPopupService } from './states-popup.service';
import { StatesService } from './states.service';

@Component({
    selector: 'jhi-states-dialog',
    templateUrl: './states-dialog.component.html'
})
export class StatesDialogComponent implements OnInit {
    states: States;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private statesService: StatesService,
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
        if (this.states.id !== undefined) {
            this.subscribeToSaveResponse(this.statesService.update(this.states));
        } else {
            this.subscribeToSaveResponse(this.statesService.create(this.states));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<States>>) {
        result.subscribe((res: HttpResponse<States>) => this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: States) {
        this.eventManager.broadcast({ name: 'statesListModification', content: 'OK' });
        this.isSaving = false;
        this.clearRoute();
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-states-popup',
    template: ''
})
export class StatesPopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private statesPopupService: StatesPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.statesPopupService.open(StatesDialogComponent as Component, params['id']);
            } else {
                this.statesPopupService.open(StatesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
