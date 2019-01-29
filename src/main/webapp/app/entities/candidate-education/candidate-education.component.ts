import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { CandidateEducation } from './candidate-education.model';
import { CandidateEducationService } from './candidate-education.service';
import { DataService, Principal } from '../../shared';
import { CandidateProfileScoreService } from '../../profiles/candidate/candidate-profile-score.service';
import { AuthoritiesConstants } from '../../shared/authorities.constant';


@Component({
    selector: 'jhi-candidate-education',
    templateUrl: './candidate-education.component.html',
    styleUrls: ['candidate-education.css']
})
export class CandidateEducationComponent implements OnInit, OnDestroy {
    candidateEducations: CandidateEducation[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    candidateId: any;
    subscription: Subscription;


    constructor(
        private candidateEducationService: CandidateEducationService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private dataService: DataService,
        private candidateProfileScoreService : CandidateProfileScoreService
    ) {
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    setRouterAddEducationParams() {
      this.dataService.setRouteData(this.candidateId);
    }
  
   setEducationRouteParam(event) {
      this.dataService.setRouteData(event);
    }
  
    loadAll() {
        if (this.currentSearch) {
            this.candidateEducationService.search({
                query: this.currentSearch,
                }).subscribe(
                        (res: HttpResponse<CandidateEducation[]>) => this.candidateEducations = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        } else {
            this.candidateEducationService.query().subscribe(
                 (res: HttpResponse<CandidateEducation[]>) => {
                this.candidateEducations = res.body;

                    this.currentSearch = '';
                },
                 (res: HttpErrorResponse) => this.onError(res.message)
            );
        }
    }
    /*To be removed once undertsand Elastic */
    loadEducationForCandidate() {

        this.candidateEducationService.findEducationByCandidateId(this.candidateId).subscribe(
          (res: HttpResponse<CandidateEducation[]>) => {
           this.candidateEducations = res.body;
            if(this.candidateEducations && this.candidateEducations.length>0) {
              this.candidateProfileScoreService.changeScore(this.candidateEducations[0].candidate.profileScore);
            } 
          },
          (res: HttpErrorResponse) => this.onError(res.message)
        );
        return;
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
            if (account.authorities.indexOf(AuthoritiesConstants.CANDIDATE) > -1) {
                this.candidateId = this.activatedRoute.snapshot.parent.data['candidate'].body.id;
                this.currentSearch = this.candidateId;
                this.loadEducationForCandidate();
            } else {
                this.loadAll();
            }
            this.registerChangeInCandidateEducations();
        });


    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CandidateEducation) {
        return item.id;
    }
    registerChangeInCandidateEducations() {

        if (this.candidateId) {

            this.eventSubscriber = this.eventManager.subscribe('candidateEducationListModification', (response) => this.loadEducationForCandidate());
            this.eventSubscriber = this.eventManager.subscribe('candidateProjectListModification', (response) => this.loadEducationForCandidate());
        } else {
            this.eventSubscriber = this.eventManager.subscribe('candidateEducationListModification', (response) => this.loadAll());
      }

    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
