import { Component, OnInit, OnDestroy } from '@angular/core';
import { Candidate } from '../../entities/candidate/candidate.model';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Subscription } from 'rxjs/Rx';

import { CandidateService } from '../../entities/candidate/candidate.service';
import { USER_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { CandidateProfileSettingService } from './candidate-profile-setting.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';

@Component({
    selector: 'jhi-candidate-primary-settings-view',
    templateUrl: 'candidate-primary-settings-view.component.html',
    styleUrls: ['candidate.css']
})
export class CandidateProfilePrimaryViewComponent implements OnInit, OnDestroy {
    candidate: Candidate;
    eventSubscriberCandidate: Subscription;
    eventSubscriberCandidateImage: Subscription;
    currentSearch: string;
    routerSub: Subscription;
    profileSubscriber: Subscription;
    candidateSettingServiceSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private jhiAlertService: JhiAlertService,
        private candidateService: CandidateService,
        private dataService: DataStorageService,
        private router: Router,
        private route: ActivatedRoute,
        private candidateSettingService: CandidateProfileSettingService
    ) {}

    ngOnInit() {
        this.profileSubscriber = this.candidateSettingService
            .getCandidateFromParentToChild()
            .subscribe(candidate => (this.candidate = candidate));
        this.profileSubscriber = this.candidateSettingService.getSetting().subscribe(settingsChanged => {
            if (settingsChanged === 'primarySetting') {
                this.reloadCandidate();
            }
        });
    }

    reloadCandidate() {
        this.candidateService.getCandidateDetails(this.dataService.getData(USER_ID)).subscribe(
            (res: HttpResponse<Candidate>) => {
                this.candidate = res.body;
                this.candidateSettingService.setCandidateFromChildToParent(this.candidate);
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        return;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
        this.router.navigate(['/error']);
    }

    /*registerChangeInCandidateData() {
    this.eventSubscriberCandidate = this.eventManager.subscribe('candidatePrimarySettingModification', (response) => this.reloadCandidate());
  }*/

    ngOnDestroy() {
        if (this.eventSubscriberCandidate) {
            this.eventManager.destroy(this.eventSubscriberCandidate);
        }
        if (this.profileSubscriber) {
            this.eventManager.destroy(this.profileSubscriber);
        }
        if (this.candidateSettingServiceSubscriber) {
            this.eventManager.destroy(this.candidateSettingServiceSubscriber);
        }
    }
}
