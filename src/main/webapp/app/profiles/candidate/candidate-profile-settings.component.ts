import { Principal } from '../../core/auth/principal.service';
import { UserService } from '../../core/user/user.service';
import { Component, OnInit, ChangeDetectorRef, AfterViewInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Candidate } from '../../entities/candidate/candidate.model';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Subscription } from 'rxjs/Rx';
import { NgbProgressbarConfig } from '@ng-bootstrap/ng-bootstrap';
import { CandidateService } from '../../entities/candidate/candidate.service';
import { JOB_ID, CORPORATE_ID, CANDIDATE_ID, USER_ID, LOGIN_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { CandidateProfileSettingService } from './candidate-profile-setting.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
    selector: 'jhi-candidate-profile',
    templateUrl: 'candidate-profile.component.html',
    styleUrls: ['candidate.css']
})
export class CandidateProfileComponent implements OnInit, AfterViewInit, OnDestroy {
    candidate: Candidate;
    imageUrl: any;
    loginId: string;
    noImage: boolean;
    userImage: string;
    eventSubscriberCandidate: Subscription;
    eventSubscriberCandidateImage: Subscription;
    currentSearch: string;
    defaultImage = require('../../../content/images/no-image.png');
    //  activeTab: string;
    profileScore: number;
    // routerSub: Subscription;
    viewMode: boolean;
    candidateProfileSubscriber: Subscription;

    constructor(
        private route: ActivatedRoute,
        private eventManager: JhiEventManager,
        private jhiAlertService: JhiAlertService,
        private candidateService: CandidateService,
        private progressBarConfig: NgbProgressbarConfig,
        private principal: Principal,
        private userService: UserService,
        private cd: ChangeDetectorRef,
        private dataService: DataStorageService,
        private router: Router,
        private candidateProfileService: CandidateProfileSettingService
    ) {
        // this.activeTab = 'details';
        this.noImage = false;
        this.progressBarConfig.max = 100;
        this.progressBarConfig.height = '10px';
        this.viewMode = true;
    }

    ngOnInit() {
        this.candidateProfileSubscriber = this.candidateProfileService.getCandidateFromChildToParent().subscribe(candidate => {
            this.candidate = candidate;
            //  console.log('------What do I get from child'+ JSON.stringify(this.candidate));
            if (!this.candidate) {
                //    console.log('---------- RELOAD-------');
                this.loadCandidate();
            }
            this.candidateProfileService.setCandidateFromParentToChild(this.candidate);
        });

        this.eventManager.broadcast({ name: 'updateNavbarImage', content: 'OK' });
        this.reloadUserImage();
        this.registerChangeInCandidateData();
        this.registerChangeInCandidateImage();
    }

    setAlerts() {
        if (this.candidate.profileScore <= 20 && !this.candidate.hasEducationScore) {
            this.jhiAlertService.addAlert({ type: 'info', msg: 'gradzcircleApp.candidate.profile.profileAlert', timeout: 5000 }, []);
        } else if (!this.candidate.hasEducationScore) {
            this.jhiAlertService.addAlert({ type: 'info', msg: 'gradzcircleApp.candidate.profile.educationAlert', timeout: 5000 }, []);
        }
    }

    ngAfterViewInit() {
        this.cd.detectChanges();
    }

    loadCandidate() {
        this.candidateService.getCandidateDetails(this.dataService.getData(USER_ID)).subscribe(
            (res: HttpResponse<Candidate>) => {
                this.candidate = res.body;
                this.candidateProfileService.setCandidateFromParentToChild(this.candidate);
                this.setAlerts();
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        return;
    }

    setPublicProfileRouteParams(candidateId, jobId, corporateId) {
        this.dataService.setdata(CANDIDATE_ID, candidateId);
        this.dataService.setdata(JOB_ID, -1);
        this.dataService.setdata(CORPORATE_ID, -1);
    }

    setPublicProfilePicmgmtRouteParams() {
        this.dataService.setdata(LOGIN_ID, this.candidate.login.id);
    }

    setRouterAddProfileCategoryParams() {
        this.dataService.setdata(CANDIDATE_ID, this.candidate.id);
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
        this.router.navigate(['/error']);
    }
    registerChangeInCandidateData() {
        this.eventSubscriberCandidate = this.eventManager.subscribe('candidateListModification', response => this.loadCandidate());
    }

    registerChangeInCandidateImage() {
        this.eventSubscriberCandidateImage = this.eventManager.subscribe('userImageModification', response => this.reloadUserImage());
    }

    ngOnDestroy() {
        if (this.eventSubscriberCandidate) {
            this.eventManager.destroy(this.eventSubscriberCandidate);
        }
        if (this.eventSubscriberCandidateImage) {
            this.eventManager.destroy(this.eventSubscriberCandidateImage);
        }
    }

    reloadUserImage() {
        this.noImage = false;
        this.principal.identity(true).then(user => {
            if (user) {
                if (user.imageUrl !== undefined) {
                    this.userService.getImageData(user.id).subscribe(response => {
                        if (response !== undefined) {
                            const responseJson = response.body;
                            if (responseJson) {
                                this.userImage = responseJson[0].href + '?t=' + Math.random().toString();
                            } else {
                                this.noImage = true;
                            }
                        }
                    });
                } else {
                    this.noImage = true;
                }
            }
        });
    }
}
