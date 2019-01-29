import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Candidate } from './candidate.model';
import { CandidatePopupService } from './candidate-popup.service';
import { CandidateService } from './candidate.service';
import { User, UserService } from '../../shared';
import { Nationality, NationalityService } from '../nationality';
import { Gender, GenderService } from '../gender';
import { MaritalStatus, MaritalStatusService } from '../marital-status';
import { JobCategory, JobCategoryService } from '../job-category';
import { Job, JobService } from '../job';
import { ProfileCategory, ProfileCategoryService } from '../profile-category';
import { Corporate, CorporateService } from '../corporate';
import { VisaType, VisaTypeService } from '../visa-type';

@Component({
    selector: 'jhi-candidate-dialog',
    templateUrl: './candidate-dialog.component.html'
})
export class CandidateDialogComponent implements OnInit {

    candidate: Candidate;
    isSaving: boolean;

    users: User[];

    nationalities: Nationality[];

    genders: Gender[];

    maritalstatuses: MaritalStatus[];

    jobcategories: JobCategory[];

    jobs: Job[];

    profilecategories: ProfileCategory[];

    corporates: Corporate[];

    visatypes: VisaType[];
    dateOfBirthDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private candidateService: CandidateService,
        private userService: UserService,
        private nationalityService: NationalityService,
        private genderService: GenderService,
        private maritalStatusService: MaritalStatusService,
        private jobCategoryService: JobCategoryService,
        private jobService: JobService,
        private profileCategoryService: ProfileCategoryService,
        private corporateService: CorporateService,
        private visaTypeService: VisaTypeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.nationalityService.query()
            .subscribe((res: HttpResponse<Nationality[]>) => { this.nationalities = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.genderService.query()
            .subscribe((res: HttpResponse<Gender[]>) => { this.genders = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.maritalStatusService.query()
            .subscribe((res: HttpResponse<MaritalStatus[]>) => { this.maritalstatuses = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.jobCategoryService.query()
            .subscribe((res: HttpResponse<JobCategory[]>) => { this.jobcategories = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.jobService.query()
            .subscribe((res: HttpResponse<Job[]>) => { this.jobs = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.profileCategoryService.query()
            .subscribe((res: HttpResponse<ProfileCategory[]>) => { this.profilecategories = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.corporateService.query()
            .subscribe((res: HttpResponse<Corporate[]>) => { this.corporates = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.visaTypeService.query()
            .subscribe((res: HttpResponse<VisaType[]>) => { this.visatypes = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.candidate.id !== undefined) {
            this.subscribeToSaveResponse(
                this.candidateService.update(this.candidate));
        } else {
            this.subscribeToSaveResponse(
                this.candidateService.create(this.candidate));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Candidate>>) {
        result.subscribe((res: HttpResponse<Candidate>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Candidate) {
        this.eventManager.broadcast({ name: 'candidateListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackNationalityById(index: number, item: Nationality) {
        return item.id;
    }

    trackGenderById(index: number, item: Gender) {
        return item.id;
    }

    trackMaritalStatusById(index: number, item: MaritalStatus) {
        return item.id;
    }

    trackJobCategoryById(index: number, item: JobCategory) {
        return item.id;
    }

    trackJobById(index: number, item: Job) {
        return item.id;
    }

    trackProfileCategoryById(index: number, item: ProfileCategory) {
        return item.id;
    }

    trackCorporateById(index: number, item: Corporate) {
        return item.id;
    }

    trackVisaTypeById(index: number, item: VisaType) {
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
    selector: 'jhi-candidate-popup',
    template: ''
})
export class CandidatePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidatePopupService: CandidatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.candidatePopupService
                    .open(CandidateDialogComponent as Component, params['id']);
            } else {
                this.candidatePopupService
                    .open(CandidateDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
