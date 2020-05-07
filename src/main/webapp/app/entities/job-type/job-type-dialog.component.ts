import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { JobType } from './job-type.model';
import { JobTypePopupService } from './job-type-popup.service';
import { JobTypeService } from './job-type.service';

@Component({
    selector: 'jhi-job-type-dialog',
    templateUrl: './job-type-dialog.component.html'
})
export class JobTypeDialogComponent implements OnInit {
    jobType: JobType;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jobTypeService: JobTypeService,
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
        if (this.jobType.id !== undefined) {
            this.subscribeToSaveResponse(this.jobTypeService.update(this.jobType));
        } else {
            this.subscribeToSaveResponse(this.jobTypeService.create(this.jobType));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<JobType>>) {
        result.subscribe((res: HttpResponse<JobType>) => this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: JobType) {
        this.eventManager.broadcast({ name: 'jobTypeListModification', content: 'OK' });
        this.isSaving = false;
        this.clearRoute();
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-job-type-popup',
    template: ''
})
export class JobTypePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private jobTypePopupService: JobTypePopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.jobTypePopupService.open(JobTypeDialogComponent as Component, params['id']);
            } else {
                this.jobTypePopupService.open(JobTypeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
