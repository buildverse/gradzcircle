import { Principal } from '../../core/auth/principal.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { NgxSpinnerService } from 'ngx-spinner';
import { CandidateLanguageProficiency } from './candidate-language-proficiency.model';
import { CandidateLanguageProficiencyService } from './candidate-language-proficiency.service';
import { AuthoritiesConstants } from '../../shared/authorities.constant';
import { CANDIDATE_ID, CANDIDATE_LANGUAGE_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { AmazonConstants } from '../../shared/amazon.constants';
@Component({
    selector: 'jhi-candidate-language-proficiency',
    templateUrl: './candidate-language-proficiency.component.html',
    styleUrls: ['candidate-language.css']
})
export class CandidateLanguageProficiencyComponent implements OnInit, OnDestroy {
    candidateLanguageProficiencies: CandidateLanguageProficiency[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    candidateId: any;
    imgSrc = AmazonConstants.S3_URL + 'languages.jpg';

    constructor(
        private candidateLanguageProficiencyService: CandidateLanguageProficiencyService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private dataService: DataStorageService,
        private spinnerService: NgxSpinnerService,
        private principal: Principal,
        private eventManager: JhiEventManager,
        private jhiAlertService: JhiAlertService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    setAddRouterParam() {
        this.dataService.setdata(CANDIDATE_ID, this.candidateId);
    }

    setEditDeleteRouterParam(candidateLanguageProficiencyId) {
        this.dataService.setdata(CANDIDATE_LANGUAGE_ID, candidateLanguageProficiencyId);
    }

    loadAll() {
        if (this.currentSearch.length > 0) {
            this.candidateLanguageProficiencyService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<CandidateLanguageProficiency[]>) => (this.candidateLanguageProficiencies = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.candidateLanguageProficiencyService.query().subscribe(
            (res: HttpResponse<CandidateLanguageProficiency[]>) => {
                this.candidateLanguageProficiencies = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
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

    loadCandidateLanguages() {
        this.spinnerService.show();
        this.candidateLanguageProficiencyService.findByCandidateId(this.candidateId).subscribe(
            (res: HttpResponse<CandidateLanguageProficiency[]>) => {
                this.candidateLanguageProficiencies = res.body;
                if (this.candidateLanguageProficiencies && this.candidateLanguageProficiencies.length <= 0) {
                    this.router.navigate(['candidate-profile/settings']);
                }
                this.spinnerService.hide();
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (account.authorities.indexOf(AuthoritiesConstants.CANDIDATE) > -1) {
                this.candidateId = this.dataService.getData(CANDIDATE_ID);
                this.loadCandidateLanguages();
            } else {
                this.loadAll();
            }
            this.registerChangeInCandidateLanguageProficiencies();
        });
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CandidateLanguageProficiency) {
        return item.id;
    }
    registerChangeInCandidateLanguageProficiencies() {
        if (this.candidateId) {
            this.eventSubscriber = this.eventManager.subscribe('candidateLanguageProficiencyListModification', response =>
                this.loadCandidateLanguages()
            );
        } else {
            this.eventSubscriber = this.eventManager.subscribe('candidateLanguageProficiencyListModification', response => this.loadAll());
        }
    }

    private onError(error) {
        this.spinnerService.hide();
        this.router.navigate(['/error']);
        this.jhiAlertService.error(error.message, null, null);
    }
}
