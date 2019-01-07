import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';
import {Principal, UserService, DataService} from '../../shared';
import {CandidatePublicProfile} from '../../entities/candidate/candidate-public-profile.model';
import {CandidateService} from '../../entities/candidate/candidate.service';
import {CandidatePublicProfilePopupService} from './candidate-public-profile-popup.service';
import {Candidate} from '../../entities/candidate/candidate.model';
import { CANDIDATE_ID, JOB_ID, CORPORATE_ID } from '../../shared/constants/storage.constants';
import {Observable} from 'rxjs/Observable';

@Component({
  selector: 'jhi-candidate-public-profile-dialog',
  templateUrl: './candidate-public-profile.html',
  styleUrls: ['candidate.css']
})
export class CandidatePublicProfilePopupDialogComponent implements OnInit {

  candidate: CandidatePublicProfile;
  defaultImage = require('../../../content/images/no-image.png');
  userImage: any;
  noImage: boolean;
  jobId: number;
  corporateId: number;
  errorMessage: String;
  isSaving: boolean;

  constructor(
    private candidateService: CandidateService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    private principal: Principal,
    private userService: UserService,
    private router: Router
  ) {
  }

  ngOnInit() {
    this.reloadUserImage();
  }

 reloadUserImage() {
    this.noImage = false;
   

      if (this.candidate.candidateDetails.login) {
        if (this.candidate.candidateDetails.login.imageUrl !== undefined) {
          this.userService.getImageData(this.candidate.candidateDetails.login.id).subscribe((response) => {
            if (response !== undefined) {
              const responseJson = response.body;
              if(responseJson) {
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


  }
  
 private getWidth (candidateLanguageProficiency) {
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
        result.subscribe((res: HttpResponse<Candidate>) =>
            this.onSaveSuccess(res.body), (res: Response) => this.onSaveError());
    }

   private onSaveSuccess(result: Candidate) {
        this.eventManager.broadcast({ name: 'matchedListModification', content: 'OK'});
      this.eventManager.broadcast({ name: 'shortListedCandidateListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

  clear() {
    this.activeModal.dismiss('cancel');
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
    private dataService : DataService
  ) {}

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      if(params['id'] && params['jobId'] && params['corporateId']) {
      this.candidatePublicProfilePopupService
        .open(CandidatePublicProfilePopupDialogComponent as Component, params['id'], params['jobId'], params['corporateId']);
      } else {
        this.candidatePublicProfilePopupService
        .open(CandidatePublicProfilePopupDialogComponent as Component, this.dataService.get(CANDIDATE_ID),
            parseFloat(this.dataService.get(JOB_ID)), parseFloat(this.dataService.get(CORPORATE_ID)));
      }
    });
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
  }
}
