import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ProfileCategory } from './profile-category.model';
import { ProfileCategoryPopupService } from './profile-category-popup.service';
import { ProfileCategoryService } from './profile-category.service';
import { Candidate, CandidateService } from '../candidate';

@Component({
    selector: 'jhi-profile-category-dialog',
    templateUrl: './profile-category-dialog.component.html'
})
export class ProfileCategoryDialogComponent implements OnInit {
    profileCategory: ProfileCategory;
    isSaving: boolean;

    candidates: Candidate[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private profileCategoryService: ProfileCategoryService,
        private candidateService: CandidateService,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.candidateService.query().subscribe(
            (res: HttpResponse<Candidate[]>) => {
                this.candidates = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.profileCategory.id !== undefined) {
            this.subscribeToSaveResponse(this.profileCategoryService.update(this.profileCategory));
        } else {
            this.subscribeToSaveResponse(this.profileCategoryService.create(this.profileCategory));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ProfileCategory>>) {
        result.subscribe(
            (res: HttpResponse<ProfileCategory>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess(result: ProfileCategory) {
        this.eventManager.broadcast({ name: 'profileCategoryListModification', content: 'OK' });
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCandidateById(index: number, item: Candidate) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-profile-category-popup',
    template: ''
})
export class ProfileCategoryPopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private profileCategoryPopupService: ProfileCategoryPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.profileCategoryPopupService.open(ProfileCategoryDialogComponent as Component, params['id']);
            } else {
                this.profileCategoryPopupService.open(ProfileCategoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
