import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { College } from './college.model';
import { CollegePopupService } from './college-popup.service';
import { CollegeService } from './college.service';
import { University, UniversityService } from '../university';

@Component({
    selector: 'jhi-college-dialog',
    templateUrl: './college-dialog.component.html'
})
export class CollegeDialogComponent implements OnInit {
    college: College;
    isSaving: boolean;

    universities: University[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private collegeService: CollegeService,
        private universityService: UniversityService,
        private eventManager: JhiEventManager,
        private router: Router
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.universityService.query().subscribe(
            (res: HttpResponse<University[]>) => {
                this.universities = res.body;
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
        if (this.college.id !== undefined) {
            this.subscribeToSaveResponse(this.collegeService.update(this.college));
        } else {
            this.subscribeToSaveResponse(this.collegeService.create(this.college));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<College>>) {
        result.subscribe((res: HttpResponse<College>) => this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: College) {
        this.eventManager.broadcast({ name: 'collegeListModification', content: 'OK' });
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

    trackUniversityById(index: number, item: University) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-college-popup',
    template: ''
})
export class CollegePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private collegePopupService: CollegePopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.collegePopupService.open(CollegeDialogComponent as Component, params['id']);
            } else {
                this.collegePopupService.open(CollegeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
