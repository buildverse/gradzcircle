import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

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
        private genderService: GenderService,
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
        if (this.gender.id !== undefined) {
            this.subscribeToSaveResponse(this.genderService.update(this.gender));
        } else {
            this.subscribeToSaveResponse(this.genderService.create(this.gender));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Gender>>) {
        result.subscribe((res: HttpResponse<Gender>) => this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Gender) {
        this.eventManager.broadcast({ name: 'genderListModification', content: 'OK' });
        this.isSaving = false;
        this.clearRoute();
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-gender-popup',
    template: ''
})
export class GenderPopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private genderPopupService: GenderPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.genderPopupService.open(GenderDialogComponent as Component, params['id']);
            } else {
                this.genderPopupService.open(GenderDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
