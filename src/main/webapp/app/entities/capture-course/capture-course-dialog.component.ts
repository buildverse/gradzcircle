import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CaptureCourse } from './capture-course.model';
import { CaptureCoursePopupService } from './capture-course-popup.service';
import { CaptureCourseService } from './capture-course.service';
import { CandidateEducation, CandidateEducationService } from '../candidate-education';

@Component({
    selector: 'jhi-capture-course-dialog',
    templateUrl: './capture-course-dialog.component.html'
})
export class CaptureCourseDialogComponent implements OnInit {
    captureCourse: CaptureCourse;
    isSaving: boolean;

    candidateeducations: CandidateEducation[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private captureCourseService: CaptureCourseService,
        private candidateEducationService: CandidateEducationService,
        private eventManager: JhiEventManager,
        private router: Router
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.candidateEducationService.query({ filter: 'capturecourse-is-null' }).subscribe(
            (res: HttpResponse<CandidateEducation[]>) => {
                if (!this.captureCourse.candidateEducation || !this.captureCourse.candidateEducation.id) {
                    this.candidateeducations = res.body;
                } else {
                    this.candidateEducationService.find(this.captureCourse.candidateEducation.id).subscribe(
                        (subRes: HttpResponse<CandidateEducation>) => {
                            this.candidateeducations = [subRes.body].concat(res.body);
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
        if (this.captureCourse.id !== undefined) {
            this.subscribeToSaveResponse(this.captureCourseService.update(this.captureCourse));
        } else {
            this.subscribeToSaveResponse(this.captureCourseService.create(this.captureCourse));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<CaptureCourse>>) {
        result.subscribe(
            (res: HttpResponse<CaptureCourse>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess(result: CaptureCourse) {
        this.eventManager.broadcast({ name: 'captureCourseListModification', content: 'OK' });
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

    trackCandidateEducationById(index: number, item: CandidateEducation) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-capture-course-popup',
    template: ''
})
export class CaptureCoursePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private captureCoursePopupService: CaptureCoursePopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.captureCoursePopupService.open(CaptureCourseDialogComponent as Component, params['id']);
            } else {
                this.captureCoursePopupService.open(CaptureCourseDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
