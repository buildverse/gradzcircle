import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService} from 'ng-jhipster';
import {JobConstants} from './job.constants';
import {Job} from './job.model';
import {JobService} from './job.service';
import {ITEMS_PER_PAGE, Principal, ResponseWrapper} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';
import {AuthoritiesConstants} from '../../shared/authorities.constant';
import {CandidateService} from '../candidate/candidate.service';
import {CorporateService} from '../corporate/corporate.service';

@Component({
  selector: 'jhi-job',
  templateUrl: './job.component.html'
})
export class JobComponent implements OnInit, OnDestroy {
  jobs: Job[];
  currentAccount: any;
  corporateId: number;
  eventSubscriber: Subscription;
  currentSearch: string;
  DRAFT: number;

  constructor(
    private jobService: JobService,
    private jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private activatedRoute: ActivatedRoute,
    private principal: Principal,
    private candidateService: CandidateService,
    private corporateService: CorporateService
  ) {
    this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.jobService.search({
        query: this.currentSearch,
      }).subscribe(
        (res: ResponseWrapper) => this.jobs = res.json,
        (res: ResponseWrapper) => this.onError(res.json)
        );
      return;
    }
    this.jobService.query().subscribe(
      (res: ResponseWrapper) => {
        this.jobs = res.json;
        this.currentSearch = '';
      },
      (res: ResponseWrapper) => this.onError(res.json)
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
    this.DRAFT = JobConstants.DRAFT;

    this.loadAll();
    this.principal.identity().then((account) => {
      this.currentAccount = account;
      if (account.authorities.indexOf(AuthoritiesConstants.CORPORATE) > -1) {
        this.corporateService.findCorporateByLoginId(account.id).subscribe(((response) => {
          this.corporateId = response.id;
          console.log('Corproate id in job comp ' + this.corporateId);
        }));
      }
    });
    this.registerChangeInJobs();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: Job) {
    return item.id;
  }
  registerChangeInJobs() {
    this.eventSubscriber = this.eventManager.subscribe('jobListModification', (response) => this.loadAll());
  }

  private onError(error) {
    this.jhiAlertService.error(error.message, null, null);
  }
}
