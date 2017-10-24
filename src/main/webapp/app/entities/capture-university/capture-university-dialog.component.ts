import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CaptureUniversity } from './capture-university.model';
import { CaptureUniversityPopupService } from './capture-university-popup.service';
import { CaptureUniversityService } from './capture-university.service';
import { CaptureCollege, CaptureCollegeService } from '../capture-college';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-capture-university-dialog',
    templateUrl: './capture-university-dialog.component.html'
})
export class CaptureUniversityDialogComponent implements OnInit {

    captureUniversity: CaptureUniversity;
    isSaving: boolean;

    capturecolleges: CaptureCollege[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private captureUniversityService: CaptureUniversityService,
        private captureCollegeService: CaptureCollegeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.captureCollegeService
            .query({filter: 'captureuniversity-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.captureUniversity.capturecollege || !this.captureUniversity.capturecollege.id) {
                    this.capturecolleges = res.json;
                } else {
                    this.captureCollegeService
                        .find(this.captureUniversity.capturecollege.id)
                        .subscribe((subRes: CaptureCollege) => {
                            this.capturecolleges = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.captureUniversity.id !== undefined) {
            this.subscribeToSaveResponse(
                this.captureUniversityService.update(this.captureUniversity));
        } else {
            this.subscribeToSaveResponse(
                this.captureUniversityService.create(this.captureUniversity));
        }
    }

    private subscribeToSaveResponse(result: Observable<CaptureUniversity>) {
        result.subscribe((res: CaptureUniversity) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: CaptureUniversity) {
        this.eventManager.broadcast({ name: 'captureUniversityListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCaptureCollegeById(index: number, item: CaptureCollege) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-capture-university-popup',
    template: ''
})
export class CaptureUniversityPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private captureUniversityPopupService: CaptureUniversityPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.captureUniversityPopupService
                    .open(CaptureUniversityDialogComponent as Component, params['id']);
            } else {
                this.captureUniversityPopupService
                    .open(CaptureUniversityDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
