import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CaptureUniversity } from './capture-university.model';
import { CaptureUniversityPopupService } from './capture-university-popup.service';
import { CaptureUniversityService } from './capture-university.service';
import { CaptureCollege, CaptureCollegeService } from '../capture-college';

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
        private eventManager: JhiEventManager,
        private router: Router
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.captureCollegeService.query({ filter: 'captureuniversity-is-null' }).subscribe(
            (res: HttpResponse<CaptureCollege[]>) => {
                if (!this.captureUniversity.capturecollege || !this.captureUniversity.capturecollege.id) {
                    this.capturecolleges = res.body;
                } else {
                    this.captureCollegeService.find(this.captureUniversity.capturecollege.id).subscribe(
                        (subRes: HttpResponse<CaptureCollege>) => {
                            this.capturecolleges = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
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
        if (this.captureUniversity.id !== undefined) {
            this.subscribeToSaveResponse(this.captureUniversityService.update(this.captureUniversity));
        } else {
            this.subscribeToSaveResponse(this.captureUniversityService.create(this.captureUniversity));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<CaptureUniversity>>) {
        result.subscribe(
            (res: HttpResponse<CaptureUniversity>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess(result: CaptureUniversity) {
        this.eventManager.broadcast({ name: 'captureUniversityListModification', content: 'OK' });
        this.isSaving = false;
        this.clearRoute();
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

    constructor(private route: ActivatedRoute, private captureUniversityPopupService: CaptureUniversityPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.captureUniversityPopupService.open(CaptureUniversityDialogComponent as Component, params['id']);
            } else {
                this.captureUniversityPopupService.open(CaptureUniversityDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
