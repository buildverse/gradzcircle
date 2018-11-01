import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';

import {CandidateLanguageProficiency} from '../../entities/candidate-language-proficiency/candidate-language-proficiency.model';



@Component({
  selector: 'jhi-candidate-language-proficiency',
  templateUrl: 'candidate-about-me-language-details.component.html'
})
export class CandidateLanguageProficiencyDetailsComponent implements OnInit {
  candidateLanguageProficiencies: CandidateLanguageProficiency[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;
  candidateId: number;

  constructor(
    private activatedRoute: ActivatedRoute,
  ) {
    this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
  }


  ngOnInit() {
    this.candidateId = this.activatedRoute.parent.parent.snapshot.data['candidate'].id;
    this.candidateLanguageProficiencies = this.activatedRoute.snapshot.data['candidateLanguageProficiency'];
    if (!this.candidateLanguageProficiencies) {
      this.candidateLanguageProficiencies = [];
    }
  }


}
