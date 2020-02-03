import { Principal } from '../../core/auth/principal.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { CandidateSkills } from './candidate-skills.model';
import { CandidateSkillsService } from './candidate-skills.service';
import { AuthoritiesConstants } from '../../shared/authorities.constant';
import { CANDIDATE_ID, CANDIDATE_SKILL_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
    selector: 'jhi-candidate-skills',
    templateUrl: './candidate-skills.component.html'
})
export class CandidateSkillsComponent implements OnInit, OnDestroy {
    candidateSkills: CandidateSkills[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    candidateId: any;

    constructor(
        private candidateSkillsService: CandidateSkillsService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private dataservice: DataStorageService,
        private spinnerService: NgxSpinnerService,
        private router: Router
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.candidateSkillsService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<CandidateSkills[]>) => (this.candidateSkills = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.candidateSkillsService.query().subscribe(
            (res: HttpResponse<CandidateSkills[]>) => {
                this.candidateSkills = res.body;
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
    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (account.authorities.indexOf(AuthoritiesConstants.CANDIDATE) > -1) {
                this.candidateId = this.dataservice.getData(CANDIDATE_ID);
                this.currentSearch = this.candidateId;
                this.loadCandidateSkills();
            } else {
                this.loadAll();
            }
            this.registerChangeInCandidateSkills();
        });
    }

    setRemoveIdForSkill(id) {
        this.dataservice.setdata(CANDIDATE_SKILL_ID, id);
    }

    loadCandidateSkills() {
        this.spinnerService.show();
        this.candidateSkillsService.findSkillsByCandidateId(this.candidateId).subscribe(
            (res: HttpResponse<CandidateSkills[]>) => {
                this.candidateSkills = res.body;
                // console.log('Certications are '+JSON.stringify(this.candidateCertifications));
                if (this.candidateSkills && this.candidateSkills.length <= 0) {
                    this.router.navigate(['./candidate-profile']);
                }
                this.spinnerService.hide();
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        return;
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CandidateSkills) {
        return item.id;
    }

    registerChangeInCandidateSkills() {
        if (this.candidateId) {
            this.eventSubscriber = this.eventManager.subscribe('candidateSkillsListModification', response => this.loadCandidateSkills());
        } else {
            this.eventSubscriber = this.eventManager.subscribe('candidateSkillsListModification', response => this.loadAll());
        }
    }

    private onError(error) {
        this.router.navigate(['/error']);
        this.jhiAlertService.error(error.message, null, null);
    }
}
