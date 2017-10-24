import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { CandidateLanguageProficiency } from '../../entities/candidate-language-proficiency/candidate-language-proficiency.model';
import { CandidateLanguageProficiencyService } from '../../entities/candidate-language-proficiency/candidate-language-proficiency.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-candidate-language-proficiency',
    templateUrl: 'candidate-about-me-language-details.component.html'
})
export class CandidateLanguageProficiencyDetailsComponent implements OnInit {
    candidateLanguageProficiencies: CandidateLanguageProficiency[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    candidateId : number;

    constructor(
        private candidateLanguageProficiencyService: CandidateLanguageProficiencyService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

   
    ngOnInit() {
        this.candidateId = this.activatedRoute.parent.parent.snapshot.data['candidate'].id;
        this.candidateLanguageProficiencies = this.activatedRoute.snapshot.data['candidateLanguageProficiency'];
       // console.log("Candidate Id in langusge "+ JSON.stringify(this.candidateId));
        if(!this.candidateLanguageProficiencies)
            this.candidateLanguageProficiencies = [];
       // console.log("Langu is "+ JSON.stringify(this.candidateLanguageProficiencies));
    }

  
}
