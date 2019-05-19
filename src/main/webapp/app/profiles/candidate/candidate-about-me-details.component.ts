import { Component, OnInit, OnDestroy} from '@angular/core';
import { Candidate } from '../../entities/candidate/candidate.model';
import 'rxjs/add/operator/debounceTime';
import { ActivatedRoute} from '@angular/router';
import {DataStorageService} from '../../shared';
import { CANDIDATE_ID } from '../../shared/constants/storage.constants';
import { CandidateProfileScoreService } from './candidate-profile-score.service';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';

@Component({
    selector: 'jhi-candidate-details',
    templateUrl: 'candidate-about-me-details.component.html',
    styleUrls : ['candidate.css']
})

export class CandidateProfileAboutMeDetailsComponent implements OnInit{

    routeSub: any;
    candidate: Candidate;
    subscription: Subscription;
    errorMessage: String;
    constructor(
        private route: ActivatedRoute,
      private dataService: DataStorageService,
      private candidateProfileScoreService : CandidateProfileScoreService
    ) {}

    ngOnInit() { 
        this.candidate = this.route.snapshot.data['candidate'].body;
        this.candidateProfileScoreService.changeScore(this.candidate.profileScore);
    }

  setRouteParams() {
    this.dataService.setdata(CANDIDATE_ID,this.candidate.id);
  }
}