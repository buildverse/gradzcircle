import {Component, OnInit, ChangeDetectorRef, AfterViewInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Candidate} from '../../entities/candidate/candidate.model';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';
import {Subscription} from 'rxjs/Rx';
import {Principal, UserService, DataStorageService} from '../../shared';
import {NgbProgressbarConfig} from '@ng-bootstrap/ng-bootstrap';
import {CandidateService} from '../../entities/candidate/candidate.service';
import {CandidateProfileScoreService} from './candidate-profile-score.service';
import {JOB_ID, CORPORATE_ID, CANDIDATE_ID, USER_ID} from '../../shared/constants/storage.constants';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
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
  profileSubscriber: Subscription;
  routerSub: Subscription;
  viewMode: boolean;

  constructor(
    private route: ActivatedRoute,
    private eventManager: JhiEventManager,
    private jhiAlertService: JhiAlertService,
    private candidateService: CandidateService,
    private progressBarConfig: NgbProgressbarConfig,
    private principal: Principal,
    private userService: UserService,
    private candidateProfileScoreService: CandidateProfileScoreService,
    private cd: ChangeDetectorRef,
    private dataService: DataStorageService,
    private router: Router
  ) {
    // this.activeTab = 'details';
    this.noImage = false;
    this.progressBarConfig.max = 100;
    this.progressBarConfig.height = '10px';
    this.viewMode = true;
  }

  ngOnInit() {
    this.routerSub = this.route.data.subscribe((data: {candidate: any}) => this.candidate = data.candidate.body);
    this.eventManager.broadcast({name: 'updateNavbarImage', content: 'OK'});
    this.reloadUserImage();
    this.registerChangeInCandidateData();
    this.registerChangeInCandidateImage();
   /* this.profileSubscriber = this.candidateProfileScoreService.currentMessage.subscribe((profileScore) => {
      if (profileScore) {
        this.profileScore = profileScore;
      }
    });*/
  }

  /* setMode() {
      this.viewMode = !this.viewMode;
      console.log('View mode is '+ this.viewMode);
  }*/

  setAlerts() {
    if (this.candidate.profileScore <= 20 && !this.candidate.hasEducationScore) {
      this.jhiAlertService.addAlert({type: 'info', msg: 'gradzcircleApp.candidate.profile.profileAlert', timeout: 5000}, []);
    } else if (!this.candidate.hasEducationScore) {
      this.jhiAlertService.addAlert({type: 'info', msg: 'gradzcircleApp.candidate.profile.educationAlert', timeout: 5000}, []);
    }
  }

  ngAfterViewInit() {
    this.cd.detectChanges();

  }

  reloadCandidate() {
   // console.log('Reloading candidate??');
    this.candidateService.getCandidateDetails(this.dataService.getData(USER_ID)).subscribe(
      (res: HttpResponse<Candidate>) => {
        this.candidate = res.body;
       // console.log('Candidate is '+JSON.stringify(this.candidate));
        this.setAlerts();
        this.candidateProfileScoreService.changeScore(this.candidate.profileScore);
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
    this.dataService.setdata(USER_ID, this.candidate.login.id);
  }

 setRouterAddProfileCategoryParams() {
    this.dataService.setdata(CANDIDATE_ID, this.candidate.id);
  }

  private onError(error) {
    this.jhiAlertService.error(error.message, null, null);
    this.router.navigate(['/error']);
  }
  registerChangeInCandidateData() {
    this.eventSubscriberCandidate = this.eventManager.subscribe('candidateListModification', (response) => this.reloadCandidate());
  }

  registerChangeInCandidateImage() {
    this.eventSubscriberCandidateImage = this.eventManager.subscribe('userImageModification', (response) => this.reloadUserImage());
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriberCandidate);
    this.eventManager.destroy(this.eventSubscriberCandidateImage);
   // this.eventManager.destroy(this.profileSubscriber);
    this.eventManager.destroy(this.routerSub);
    // console.log('Destroing parent profile');
  }

  reloadUserImage() {
    this.noImage = false;
    this.principal.identity(true).then((user) => {

      if (user) {
        if (user.imageUrl !== undefined) {
          this.userService.getImageData(user.id).subscribe((response) => {
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
