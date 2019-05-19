import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService} from 'ng-jhipster';
import {AuthoritiesConstants} from '../../shared/authorities.constant';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import {CandidateNonAcademicWork} from './candidate-non-academic-work.model';
import {CandidateNonAcademicWorkService} from './candidate-non-academic-work.service';
import {DataStorageService, Principal} from '../../shared';
import {NgxSpinnerService} from 'ngx-spinner';
import {CandidateProfileScoreService} from '../../profiles/candidate/candidate-profile-score.service';
import { CANDIDATE_ID, CANDIDATE_NON_ACADEMIC_ID } from '../../shared/constants/storage.constants';
@Component({
  selector: 'jhi-candidate-non-academic-work',
  templateUrl: './candidate-non-academic-work.component.html',
  styleUrls: ['non-academic.css']
})
export class CandidateNonAcademicWorkComponent implements OnInit, OnDestroy {
  candidateNonAcademicWorks: CandidateNonAcademicWork[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;
  candidateId: any;

  constructor(
    private candidateNonAcademicWorkService: CandidateNonAcademicWorkService,
    private jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private activatedRoute: ActivatedRoute,
    private principal: Principal,
    private dataService: DataStorageService,
    private candidateProfileScoreService: CandidateProfileScoreService,
    private spinnerService: NgxSpinnerService
  ) {
    this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
      this.activatedRoute.snapshot.params['search'] : '';
  }
  /*To be removed once undertsand Elastic */
  loadExtraCurricularForCandidate() {
    this.spinnerService.show();  
    this.candidateNonAcademicWorkService.findNonAcademicWorkByCandidateId(this.candidateId).subscribe(
      (res: HttpResponse<CandidateNonAcademicWork[]>) => {
        this.candidateNonAcademicWorks = res.body
        if (this.candidateNonAcademicWorks && this.candidateNonAcademicWorks.length > 0) {
          this.candidateProfileScoreService.changeScore(this.candidateNonAcademicWorks[0].candidate.profileScore);
        }
        this.spinnerService.hide();
      },

      (res: HttpErrorResponse) => this.onError(res.message)
    );
    return;
  }

  setAddRouterParam() {
    this.dataService.setdata(CANDIDATE_ID,this.candidateId);
  }

  setEditDeleteRouteParam(candidateNonAcademicWorkId) {
    this.dataService.setdata(CANDIDATE_NON_ACADEMIC_ID,candidateNonAcademicWorkId);
  }

  loadAll() {
    if (this.currentSearch) {
      this.candidateNonAcademicWorkService.search({
        query: this.currentSearch,
      }).subscribe(
        (res: HttpResponse<CandidateNonAcademicWork[]>) => this.candidateNonAcademicWorks = res.body,
        (res: HttpErrorResponse) => this.onError(res.message)
        );
      return;
    }
    this.candidateNonAcademicWorkService.query().subscribe(
      (res: HttpResponse<CandidateNonAcademicWork[]>) => {
        this.candidateNonAcademicWorks = res.body;

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
    this.principal.identity().then((account) => {
      this.currentAccount = account;
      if (account.authorities.indexOf(AuthoritiesConstants.CANDIDATE) > -1) {
        this.candidateId = this.activatedRoute.snapshot.parent.data['candidate'].body.id;
        this.currentSearch = this.candidateId;
        this.loadExtraCurricularForCandidate();
      } else {
        this.loadAll();
      }
      this.registerChangeInCandidateNonAcademicWorks();
    });

  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: CandidateNonAcademicWork) {
    return item.id;
  }

  registerChangeInCandidateNonAcademicWorks() {
    if (this.candidateId) {
      this.eventSubscriber = this.eventManager.subscribe('candidateNonAcademicWorkListModification', (response) => this.loadExtraCurricularForCandidate());
    } else {
      this.eventSubscriber = this.eventManager.subscribe('candidateNonAcademicWorkListModification', (response) => this.loadAll());
    }
  }

  private onError(error) {
    this.spinnerService.hide();
    this.jhiAlertService.error(error.message, null, null);
  }
}
