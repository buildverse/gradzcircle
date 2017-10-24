import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Candidate } from './candidate.model';
import { CandidatePopupService } from './candidate-popup.service';
import { CandidateService } from './candidate.service';
import { User, UserService } from '../../shared';
import { Gender, GenderService } from '../gender';
import { MaritalStatus, MaritalStatusService } from '../marital-status';
import { Nationality, NationalityService } from '../nationality';
import { Address, AddressService } from '../address';
import { JobCategory, JobCategoryService } from '../job-category';
import { VisaType, VisaTypeService } from '../visa-type';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-candidate-dialog',
    templateUrl: './candidate-dialog.component.html'
})
export class CandidateDialogComponent implements OnInit {

    candidate: Candidate;
    isSaving: boolean;

    users: User[];

    genders: Gender[];

    maritalstatuses: MaritalStatus[];

    nationalities: Nationality[];

    addresses: Address[];

    jobcategories: JobCategory[];

    visatypes: VisaType[];
    dateOfBirthDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private candidateService: CandidateService,
        private userService: UserService,
        private genderService: GenderService,
        private maritalStatusService: MaritalStatusService,
        private nationalityService: NationalityService,
        private addressService: AddressService,
        private jobCategoryService: JobCategoryService,
        private visaTypeService: VisaTypeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.genderService
            .query({filter: 'candidate-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.candidate.gender || !this.candidate.gender.id) {
                    this.genders = res.json;
                } else {
                    this.genderService
                        .find(this.candidate.gender.id)
                        .subscribe((subRes: Gender) => {
                            this.genders = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.maritalStatusService
            .query({filter: 'candidate-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.candidate.maritalStatus || !this.candidate.maritalStatus.id) {
                    this.maritalstatuses = res.json;
                } else {
                    this.maritalStatusService
                        .find(this.candidate.maritalStatus.id)
                        .subscribe((subRes: MaritalStatus) => {
                            this.maritalstatuses = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.nationalityService
            .query({filter: 'candidate-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.candidate.nationality || !this.candidate.nationality.id) {
                    this.nationalities = res.json;
                } else {
                    this.nationalityService
                        .find(this.candidate.nationality.id)
                        .subscribe((subRes: Nationality) => {
                            this.nationalities = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.addressService
            .query({filter: 'candidate-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.candidate.address || !this.candidate.address.id) {
                    this.addresses = res.json;
                } else {
                    this.addressService
                        .find(this.candidate.address.id)
                        .subscribe((subRes: Address) => {
                            this.addresses = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.jobCategoryService.query()
            .subscribe((res: ResponseWrapper) => { this.jobcategories = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.visaTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.visatypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
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

    private subscribeToSaveResponse(result: Observable<Candidate>) {
        result.subscribe((res: Candidate) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
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

    trackGenderById(index: number, item: Gender) {
        return item.id;
    }

    trackMaritalStatusById(index: number, item: MaritalStatus) {
        return item.id;
    }

    trackNationalityById(index: number, item: Nationality) {
        return item.id;
    }

    trackAddressById(index: number, item: Address) {
        return item.id;
    }

    trackJobCategoryById(index: number, item: JobCategory) {
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
