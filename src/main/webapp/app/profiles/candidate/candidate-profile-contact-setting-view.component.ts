import {Component, OnInit, OnDestroy} from '@angular/core';
import {Candidate} from '../../entities/candidate/candidate.model';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';
import {Subscription} from 'rxjs/Rx';
import {DataStorageService} from '../../shared';
import {CandidateService} from '../../entities/candidate/candidate.service';
import {USER_ID, USER_DATA} from '../../shared/constants/storage.constants';
import { CandidateProfileSettingService } from './candidate-profile-setting.service';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-candidate-primary-settings-view',
  templateUrl: 'candidate-profile-contact-setting-view.component.html',
  styleUrls: ['candidate.css']

})

export class CandidateProfileContactViewComponent implements OnInit, OnDestroy {

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
    private route : ActivatedRoute,
    private candidateSettingService: CandidateProfileSettingService
  ) {
  }

  ngOnInit() {

    this.profileSubscriber = this.candidateSettingService.getCandidateFromParentToChild().
      subscribe((candidate) => this.candidate = candidate);
    this.candidateSettingServiceSubscriber = this.candidateSettingService.getSetting()
        .subscribe((settingsChanged) => {
          if (settingsChanged === 'contactSetting') {
            this.reloadCandidate();
          }
        });
  }

  reloadCandidate() {
   // console.log('Reloading candidate??');
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
  }

  registerChangeInCandidateData() {
   // this.eventSubscriberCandidate = this.eventManager.subscribe('candidateListModification', (response) => this.reloadCandidate());
  }

    ngOnDestroy() {
        if (this.eventSubscriberCandidate) {
            this.eventManager.destroy(this.eventSubscriberCandidate);
        }
        if (this.routerSub) {
            this.eventManager.destroy(this.routerSub);
        }
      if (this.profileSubscriber) {
        this.eventManager.destroy(this.profileSubscriber);
      }
      if (this.candidateSettingServiceSubscriber) {
        this.eventManager.destroy(this.candidateSettingServiceSubscriber);
      }
    }
}
