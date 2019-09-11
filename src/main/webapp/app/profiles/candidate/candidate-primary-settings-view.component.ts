import {Component, OnInit, OnDestroy} from '@angular/core';
import {Candidate} from '../../entities/candidate/candidate.model';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';
import {Subscription} from 'rxjs/Rx';
import { DataStorageService} from '../../shared';
import {CandidateService} from '../../entities/candidate/candidate.service';
import { USER_ID} from '../../shared/constants/storage.constants';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-candidate-primary-settings-view',
  templateUrl: 'candidate-primary-settings-view.component.html',
  styleUrls: ['candidate.css']

})

export class CandidateProfilePrimaryViewComponent implements OnInit, OnDestroy {

  candidate: Candidate;
  eventSubscriberCandidate: Subscription;
  eventSubscriberCandidateImage: Subscription;
  currentSearch: string;
  routerSub: Subscription;

  constructor(
    private eventManager: JhiEventManager,
    private jhiAlertService: JhiAlertService,
    private candidateService: CandidateService,
    private dataService: DataStorageService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
    this.routerSub = this.route.parent.data.subscribe((data: {candidate: any}) => this.candidate = data.candidate.body);
    if (!this.candidate) {
      this.reloadCandidate();
    }
  }

  reloadCandidate() {
   // console.log('Reloading candidate??');
    this.candidateService.getCandidateDetails(this.dataService.getData(USER_ID)).subscribe(
      (res: HttpResponse<Candidate>) => {
        this.candidate = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    return;
  }
  private onError(error) {
    this.jhiAlertService.error(error.message, null, null);
    this.router.navigate(['/error']);
  }

  registerChangeInCandidateData() {
    this.eventSubscriberCandidate = this.eventManager.subscribe('candidateListModification', (response) => this.reloadCandidate());
  }

  ngOnDestroy() {
      if (this.eventSubscriberCandidate) {
        this.eventManager.destroy(this.eventSubscriberCandidate);
      }
  }
}
