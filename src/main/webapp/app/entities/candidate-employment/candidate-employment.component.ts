import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { CandidateEmployment } from './candidate-employment.model';
import { CandidateEmploymentService } from './candidate-employment.service';
import { DataService, Principal } from '../../shared';
import { CandidateProfileScoreService } from '../../profiles/candidate/candidate-profile-score.service';
import { AuthoritiesConstants } from '../../shared/authorities.constant';


@Component({
    selector: 'jhi-candidate-employment',
    templateUrl: './candidate-employment.component.html',
    styleUrls :['candidate-employment.css']
})
export class CandidateEmploymentComponent implements OnInit, OnDestroy {
    candidateEmployments: CandidateEmployment[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    candidateId: any;
    subscription: Subscription;
    profileScore: number; 

    constructor(
        private candidateEmploymentService: CandidateEmploymentService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private dataService: DataService,
        private candidateProfileScoreService: CandidateProfileScoreService
    ) {
          this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

  setAddRouteParams() {
    this.dataService.setRouteData(this.candidateId);
  }
  
  setEditDeleteRouteParams(candidateEmploymentId) {
    this.dataService.setRouteData(candidateEmploymentId);
  }

    /*To be removed once undertsand Elastic */
    loadEmploymentsForCandidate() {

        this.candidateEmploymentService.findEmploymentsByCandidateId(this.candidateId).subscribe(
           (res: HttpResponse<CandidateEmployment[]>) =>{
             this.candidateEmployments = res.body;
             if(this.candidateEmployments && this.candidateEmployments.length>0)
               {
               this.candidateProfileScoreService.changeScore(this.candidateEmployments[0].candidate.profileScore);
             } else {
               this.candidateProfileScoreService.changeScore(this.profileScore);
             }
           } 
             ,
                    (res: HttpErrorResponse) => this.onError(res.message));
        return;

    }

    loadAll() {
        if (this.currentSearch) {
            this.candidateEmploymentService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<CandidateEmployment[]>) => this.candidateEmployments = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        else {
            this.candidateEmploymentService.query().subscribe(
                (res: HttpResponse<CandidateEmployment[]>) => {
                this.candidateEmployments = res.body;
                this.currentSearch = '';
                },
                 (res: HttpErrorResponse) => this.onError(res.message)
            );
        }
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
            if(account.authorities.indexOf(AuthoritiesConstants.CANDIDATE)>-1){
                this.candidateId = this.activatedRoute.snapshot.parent.data['candidate'].body.id;
                this.profileScore = this.activatedRoute.snapshot.parent.data['candidate'].body.profileScore;
                this.currentSearch = this.candidateId;
                this.loadEmploymentsForCandidate();
            } else {
                this.loadAll();
            }
            this.registerChangeInCandidateEmployments();
        });

    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CandidateEmployment) {
        return item.id;
    }
    registerChangeInCandidateEmployments() {
        if (this.candidateId) {
            this.eventSubscriber = this.eventManager.subscribe('candidateEmploymentListModification', (response) => this.loadEmploymentsForCandidate());
            this.eventSubscriber = this.eventManager.subscribe('candidateProjectListModification', (response) => this.loadEmploymentsForCandidate());
        }
        else
            this.eventSubscriber = this.eventManager.subscribe('candidateEmploymentListModification', (response) => this.loadAll());

    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
