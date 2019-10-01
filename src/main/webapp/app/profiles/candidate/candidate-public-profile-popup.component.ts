import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HttpResponse} from '@angular/common/http';
import {NgbActiveModal, NgbRatingConfig} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';
import {UserService, DataStorageService} from '../../shared';
import {CandidatePublicProfile} from '../../entities/candidate/candidate-public-profile.model';
import {CandidateService} from '../../entities/candidate/candidate.service';
import {CandidatePublicProfilePopupService} from './candidate-public-profile-popup.service';
import {Candidate} from '../../entities/candidate/candidate.model';
import { AuthoritiesConstants } from '../../shared/authorities.constant';
import {CANDIDATE_ID, JOB_ID, CORPORATE_ID, FROM_LINKED_CANDIDATE, BUSINESS_PLAN_ENABLED, USER_TYPE} from '../../shared/constants/storage.constants';
import { Subscription } from 'rxjs';
import {Observable} from 'rxjs/Observable';

@Component({
  selector: 'jhi-candidate-public-profile-dialog',
  templateUrl: './candidate-public-profile.html',
  styleUrls: ['candidate.css']
})
export class CandidatePublicProfilePopupDialogComponent implements OnInit, OnDestroy {

  candidate: CandidatePublicProfile;
  defaultImage = require('../../../content/images/no-image.png');
  userImage: any;
  noImage: boolean;
  jobId: number;
  corporateId: number;
  errorMessage: String;
  isSaving: boolean;
  rating: number;
  fromLinkedCandidate: boolean;
  eventSubscriber: Subscription;
  imageSubscriber: Subscription;
  businessPlanEnabled?: boolean;
  canCorporateViewDetails?: boolean;
  
  constructor(
    private candidateService: CandidateService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    private userService: UserService,
    public ratingConfig: NgbRatingConfig,
    public alertService: JhiAlertService,
    private dataService: DataStorageService
  ) {
    this.ratingConfig.max = 5;
    this.ratingConfig.readonly = true;
    this.canCorporateViewDetails = false;
  }

  ngOnInit() {
      this.businessPlanEnabled = this.dataService.getData(BUSINESS_PLAN_ENABLED) === 'true' ? true : false;
    this.reloadUserImage();
    this.alertService.clear();
    if (this.dataService.getData(USER_TYPE) === AuthoritiesConstants.CORPORATE ) {
      this.canCorporateViewDetails = this.candidate.isShortListed;
      if (this.candidate.reviewed && !this.candidate.isShortListed) {
        this.alertService.addAlert({type: 'info', msg: 'gradzcircleApp.candidate.profile.reviewAlert'}, []);
      }
      if (!this.candidate.isShortListed) {
        this.alertService.addAlert({type: 'info', msg: 'gradzcircleApp.candidate.profile.notShortListedAlert'}, [])
      }
      if (!this.candidate.canBeShortListed && this.businessPlanEnabled && !this.candidate.isShortListed) {
        this.alertService.addAlert({type: 'danger', msg: 'gradzcircleApp.job.topUpAlert'}, []);
      } else if (!this.candidate.canBeShortListed && !this.businessPlanEnabled) {
        this.alertService.addAlert({type: 'danger', msg: 'gradzcircleApp.job.requestMoreCandidateAlert'}, []);
      }
    } else if (this.dataService.getData(USER_TYPE) === AuthoritiesConstants.CANDIDATE) {
        this.alertService.addAlert({type: 'info', msg: 'gradzcircleApp.candidate.profile.publicProfileMessage', timeout: 5000 }, []);
    }
  }

  setMatchConfigRate() {
    if (this.candidate.matchScore >= 85 && this.candidate.matchScore < 101) {
      this.rating = 5;
    } else if (this.candidate.matchScore >= 71 && this.candidate.matchScore < 85) {
      this.rating = 4;
    } else if (this.candidate.matchScore >= 55 && this.candidate.matchScore < 71) {
      this.rating = 3;
    } else if (this.candidate.matchScore < 55) {
      this.rating = 2;
    }
  }

  reloadUserImage() {
    this.noImage = false;
    if (this.candidate.candidateDetails.login) {
      if (this.candidate.candidateDetails.login.imageUrl !== undefined) {
        this.imageSubscriber = this.userService.getImageData(this.candidate.candidateDetails.login.id).subscribe((response) => {
          if (response !== undefined) {
            const responseJson = response.body;
            if (responseJson) {
              this.userImage = responseJson[0].href + '?t=' + Math.random().toString();
            } else {
              this.noImage = true;
              this.userImage = this.defaultImage;
            }
          }
        });
      } else {
        this.noImage = true;
      }
    }
  }

  private getWidth(candidateLanguageProficiency) {
    if (candidateLanguageProficiency === 'Beginner') {
      return '33%';
    } else if (candidateLanguageProficiency === 'Intermediate') {
      return '66%';
    } else if (candidateLanguageProficiency === 'Expert') {
      return '100%';
    }
  }

  save() {
    this.isSaving = true;

    this.subscribeToSaveResponse(this.candidateService.linkCandidateAndCorporateForJob(this.candidate.candidateDetails.id, this.jobId, this.corporateId));

  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<Candidate>>) {
    this.eventSubscriber = result.subscribe((res: HttpResponse<Candidate>) =>
      this.onSaveSuccess(res.body), (res: Response) => this.onSaveError());
  }

  private onSaveSuccess(result: Candidate) {
    this.eventManager.broadcast({name: 'matchedListModification', content: 'OK'});
    this.eventManager.broadcast({name: 'shortListedCandidateListModification', content: 'OK'});
    this.eventManager.broadcast({name: 'jobListModification', content: 'OK'});
    this.isSaving = false;
    this.canCorporateViewDetails = !this.canCorporateViewDetails;
   // this.activeModal.dismiss(result);
    this.alertService.clear();
    this.alertService.addAlert({type: 'success', msg: 'gradzcircleApp.job.candidateLinkedAlert', timeout: 5000 }, []);
  }

  private onSaveError() {
    this.isSaving = false;
  }

  ngOnDestroy() {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
    if (this.imageSubscriber) {
      this.eventManager.destroy(this.imageSubscriber); 

    }
    this.alertService.clear();
  }

  clear() {
    this.dataService.removeData(FROM_LINKED_CANDIDATE);
    this.activeModal.dismiss('cancel');
    this.alertService.clear();
  }
}

@Component({
  selector: 'jhi-candidate-public-profile-popup',
  template: ''
})
export class CandidatePublicProfilePopupComponent implements OnInit, OnDestroy {

  routeSub: any;

  constructor(
    private route: ActivatedRoute,
    private candidatePublicProfilePopupService: CandidatePublicProfilePopupService,
    private dataService: DataStorageService
  ) {}

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      if (params['id'] && params['jobId'] && params['corporateId']) {
        this.candidatePublicProfilePopupService
          .open(CandidatePublicProfilePopupDialogComponent as Component, params['id'], params['jobId'], params['corporateId']);
      } else {
        this.candidatePublicProfilePopupService
          .open(CandidatePublicProfilePopupDialogComponent as Component, this.dataService.getData(CANDIDATE_ID),
          parseFloat(this.dataService.getData(JOB_ID)), parseFloat(this.dataService.getData(CORPORATE_ID)),this.dataService.getData(FROM_LINKED_CANDIDATE));
      }
    });
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
  }
}
