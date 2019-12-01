import { Principal } from '../../core/auth/principal.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { CandidateEmployment } from './candidate-employment.model';
import { CandidateEmploymentService } from './candidate-employment.service';
import { AuthoritiesConstants } from '../../shared/authorities.constant';
import {
    CANDIDATE_ID,
    CANDIDATE_EMPLOYMENT_ID,
    CANDIDATE_PROJECT_ID,
    IS_EMPLOYMENT_PROJECT
} from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
    selector: 'jhi-candidate-employment',
    templateUrl: './candidate-employment.component.html',
    styleUrls: ['candidate-employment.css']
})
export class CandidateEmploymentComponent implements OnInit, OnDestroy {
    candidateEmployments: CandidateEmployment[];
    currentAccount: any;
    employemntEventSubscriber: Subscription;
    projectEventSubscriber: Subscription;
    currentSearch: string;
    candidateId: any;
    subscription: Subscription;
    profileScore: number;
    candidateEmploymentsForDisplay: CandidateEmployment[];
    primaryCandidateEmployment: CandidateEmployment;

    constructor(
        private candidateEmploymentService: CandidateEmploymentService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private router: Router,
        private dataService: DataStorageService,
        private spinnerService: NgxSpinnerService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    /*  setAddRouteParams() {
      this.dataService.setdata(CANDIDATE_ID,this.candidateId);
    }
    */
    setEditDeleteRouteParams(candidateEmploymentId) {
        this.dataService.setdata(CANDIDATE_EMPLOYMENT_ID, candidateEmploymentId);
    }

    setProjectRouteParam(event) {
        this.dataService.setdata(CANDIDATE_PROJECT_ID, event);
        this.dataService.setdata(IS_EMPLOYMENT_PROJECT, 'true');
    }

    setAddRouteParams() {
        this.dataService.setdata(CANDIDATE_ID, this.candidateId);
    }

    loadEmploymentsForCandidate() {
        this.spinnerService.show();
        this.candidateEmploymentService.findEmploymentsByCandidateId(this.candidateId).subscribe(
            (res: HttpResponse<CandidateEmployment[]>) => {
                this.candidateEmployments = res.body;
                // console.log('Employment are '+JSON.stringify(this.candidateEmployments));
                if (this.candidateEmployments && this.candidateEmployments.length <= 0) {
                    this.router.navigate(['./candidate-profile']);
                }
                this.candidateEmploymentsOnLoad();
                /* if (this.candidateEmployments && this.candidateEmployments.length > 0) {
          this.candidateProfileScoreService.changeScore(this.candidateEmployments[0].candidate.profileScore);
        } else {
          this.candidateProfileScoreService.changeScore(this.profileScore);
        }*/
                this.spinnerService.hide();
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        return;
    }

    candidateEmploymentsOnLoad() {
        this.candidateEmploymentsForDisplay = [];
        this.primaryCandidateEmployment = undefined;
        // console.log('candiate Educaiton from server is'+JSON.stringify(this.candidateEducations));
        // console.log('candiate Educaiton locally'+JSON.stringify(this.candidateEducationsForDisplay));
        if (this.candidateEmployments && this.candidateEmployments.length > 0) {
            this.candidateEmployments.forEach(employment => {
                if (!this.primaryCandidateEmployment) {
                    this.primaryCandidateEmployment = employment;
                } else {
                    this.candidateEmploymentsForDisplay.push(employment);
                }
            });
        }
        //   console.log('candiate Educaiton from server in end is'+JSON.stringify(this.candidateEducations));
        // console.log('candiate Educaiton locally in end is '+JSON.stringify(this.candidateEducationsForDisplay));
    }

    setPrimaryByUserSelection(event) {
        this.candidateEmploymentsForDisplay = [];
        this.primaryCandidateEmployment = undefined;
        if (this.candidateEmployments && this.candidateEmployments.length > 0) {
            this.candidateEmployments.forEach(employment => {
                if (employment.id === event) {
                    this.primaryCandidateEmployment = employment;
                } else {
                    this.candidateEmploymentsForDisplay.push(employment);
                }
            });
        }
        window.scroll(0, 0);
    }

    loadAll() {
        if (this.currentSearch) {
            this.candidateEmploymentService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<CandidateEmployment[]>) => (this.candidateEmployments = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        } else {
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
        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (account.authorities.indexOf(AuthoritiesConstants.CANDIDATE) > -1) {
                /* this.candidateId = this.activatedRoute.snapshot.parent.data['candidate'].body.id;
         this.profileScore = this.activatedRoute.snapshot.parent.data['candidate'].body.profileScore;
         this.currentSearch = this.candidateId;*/

                this.candidateId = this.dataService.getData(CANDIDATE_ID);
                this.loadEmploymentsForCandidate();
            } else {
                this.loadAll();
            }
            this.registerChangeInCandidateEmployments();
        });
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.employemntEventSubscriber);
        this.eventManager.destroy(this.projectEventSubscriber);
    }

    trackId(index: number, item: CandidateEmployment) {
        return item.id;
    }
    registerChangeInCandidateEmployments() {
        if (this.candidateId) {
            this.employemntEventSubscriber = this.eventManager.subscribe('candidateEmploymentListModification', response =>
                this.loadEmploymentsForCandidate()
            );
            this.projectEventSubscriber = this.eventManager.subscribe('candidateProjectListModification', response =>
                this.loadEmploymentsForCandidate()
            );
        } else {
            this.employemntEventSubscriber = this.eventManager.subscribe('candidateEmploymentListModification', response => this.loadAll());
        }
    }

    private onError(error) {
        this.spinnerService.hide();
        this.jhiAlertService.error(error.message, null, null);
    }
}
