import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';
import {Principal, UserService} from '../../shared';
import {CandidatePublicProfile} from '../../entities/candidate/candidate-public-profile.model';
import {CandidateService} from '../../entities/candidate/candidate.service';
import {CandidatePublicProfilePopupService} from './candidate-public-profile-popup.service';
import {Candidate} from '../../entities/candidate/candidate.model';
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
    this.principal.identity().then((user) => {
      if (user) {
        if (user.imageUrl) {
          this.userService.getImageData(user.id).subscribe(response => {
            let responseJson = response.body;
            this.userImage = responseJson[0].href + '?t=' + Math.random().toString();
          }, (error: any) => {
            console.log(`${error}`);
            this.router.navigate(['/error']);
            return Observable.of(null);
          });
        }
        else {
          this.noImage = true;
        }
      }
    });
  }

  private getWidth(candidateLanguageProficiency) {
    if (candidateLanguageProficiency === 'Beginner')
      return '33%';
    else if (candidateLanguageProficiency === 'Intermediate')
      return '66%';
    else if (candidateLanguageProficiency === 'Expert')
      return '100%';
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
        this.isSaving = false;
     console.log('Save is a success ??');
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
    private candidatePublicProfilePopupService: CandidatePublicProfilePopupService
  ) {}

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      this.candidatePublicProfilePopupService
        .open(CandidatePublicProfilePopupDialogComponent as Component, params['id'], params['jobId'], params['corporateId']);
    });
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
  }
}
