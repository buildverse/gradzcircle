import { Component, OnInit, Output,EventEmitter} from '@angular/core';
import { Candidate } from '../../entities/candidate/candidate.model';
import 'rxjs/add/operator/debounceTime';
import { ActivatedRoute} from '@angular/router';
import {DataService} from '../../shared';
import { CandidateProfileScoreService } from './candidate-profile-score.service';

@Component({
    selector: 'jhi-candidate-details',
    templateUrl: 'candidate-about-me-details.component.html',
    styleUrls : ['candidate.css']
})

export class CandidateProfileAboutMeDetailsComponent implements OnInit {

    candidate: Candidate;

    errorMessage: String;
    constructor(
        private route: ActivatedRoute,
      private dataService: DataService,
      private candidateProfileScoreService : CandidateProfileScoreService
    ) {}

    ngOnInit() { 
        this.candidate = this.route.snapshot.data['candidate'].body;
        this.candidateProfileScoreService.changeScore(this.candidate.profileScore);
    }
  
  setRouteParams() {
    this.dataService.setRouteData(this.candidate.id);
  }
}