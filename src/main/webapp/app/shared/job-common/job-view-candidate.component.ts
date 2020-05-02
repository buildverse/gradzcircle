import { Principal } from '../../core/auth/principal.service';
import { LoginModalService } from '../../core/login/login-modal.service';
import { LoginService } from '../../core/login/login.service';
import { UserService } from '../../core/user/user.service';
import { JobListEmitterService } from '../../entities/job/job-list-change.service';
import { JobPopupService } from '../../entities/job/job-popup.service';
import { JobService } from '../../entities/job/job.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { JOB_ID, CANDIDATE_ID, BUSINESS_PLAN_ENABLED } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { Job } from '../../shared/job-common/job.model';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Observable } from 'rxjs/Rx';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-job-view',
    templateUrl: './job-view.component.html'
})
export class JobViewForCandidateComponent implements OnInit {
    noImage: boolean;
    imageUrl: string;
    businessPlanEnabled: boolean;
    job: Job;
    currentAccount: any;
    isSaving: boolean;
    modalRef: NgbModalRef;
    profile: string;

    constructor(
        private jobService: JobService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router,
        private principal: Principal,
        private jhiAlertService: JhiAlertService,
        private jobListEmitterService: JobListEmitterService,
        private userService: UserService,
        private loginService: LoginService,
        private loginModalService: LoginModalService,
        private localDataStorageService: DataStorageService
    ) {
        this.profile = '';
    }

    ngOnInit() {
        this.businessPlanEnabled = JSON.parse(this.localDataStorageService.getData(BUSINESS_PLAN_ENABLED));
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.loadCorporateImage();
    }

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    clearRoute() {
        if (this.profile === 'applied' || this.profile === 'shortlisted') {
            this.router.navigate(['/candidate-profile', { outlets: { popup: null } }]);
        } else if (this.profile === 'viewJobs') {
            this.router.navigate(['/viewjobs', { outlets: { popup: null } }]);
        }
    }

    login() {
        this.clear();
        this.modalRef = this.loginModalService.open();
    }

    applyForJob(jobId) {
        this.isSaving = true;
        this.subscribeToSaveResponse(this.jobService.applyforJob(jobId, this.currentAccount.id));
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Job>>) {
        result.subscribe((res: HttpResponse<Job>) => this.onSaveSuccess(res.body), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Job) {
        this.eventManager.broadcast({ name: 'jobListModification', content: 'OK' });
        this.jobListEmitterService.jobChanges(true);
        this.clearRoute();
        this.activeModal.dismiss();
    }

    private onSaveError(error: any) {
        this.onError(error);
    }

    private onError(error: any) {
        this.router.navigate(['/error']);
        this.activeModal.dismiss();
        this.jhiAlertService.error(error.message, null, null);
    }

    loadCorporateImage() {
        if (this.job.corporateUrl) {
            this.userService.getImageData(this.job.corporateLoginId).subscribe(response => {
                const responseJson = response.body;
                this.imageUrl = responseJson[0].href + '?t=' + Math.random().toString();
            });
            this.noImage = false;
        } else {
            this.noImage = true;
        }
    }
}

@Component({
    selector: 'jhi-job-view-popup',
    template: ''
})
export class JobViewPopupForCandidateComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private jobPopupService: JobPopupService, private dataService: DataStorageService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.jobPopupService.openForCandidateView(
                    JobViewForCandidateComponent as Component,
                    params['id'],
                    params['candidateId'],
                    params['userProfile']
                );
            } else {
                let id = this.dataService.getData(JOB_ID);

                if (!id) {
                    id = this.dataService.getData(JOB_ID);
                }
                let candidateID = this.dataService.getData(CANDIDATE_ID);
                if (!candidateID) {
                    candidateID = -1;
                }
                this.jobPopupService.openForCandidateView(
                    JobViewForCandidateComponent as Component,
                    id,
                    candidateID,
                    params['userProfile']
                );
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
