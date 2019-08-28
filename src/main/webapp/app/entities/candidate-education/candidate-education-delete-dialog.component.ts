import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {DataStorageService} from '../../shared';
import {CANDIDATE_EDUCATION_ID, USER_ID, USER_DATA} from '../../shared/constants/storage.constants';
import {Candidate} from '../candidate';
import {CandidateService} from '../candidate/candidate.service';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';
import {NgxSpinnerService} from 'ngx-spinner';
import {CandidateEducation} from './candidate-education.model';
import {CandidateEducationPopupService} from './candidate-education-popup.service';
import {CandidateEducationService} from './candidate-education.service';
import {HttpErrorResponse} from '@angular/common/http';
import {HttpResponse} from '@angular/common/http';

@Component({
  selector: 'jhi-candidate-education-delete-dialog',
  templateUrl: './candidate-education-delete-dialog.component.html'
})
export class CandidateEducationDeleteDialogComponent {

  candidateEducation: CandidateEducation;

  constructor(
    private candidateEducationService: CandidateEducationService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    private spinnerService: NgxSpinnerService,
    private candidateService: CandidateService,
    private dataService: DataStorageService,
    private jhiAlertService: JhiAlertService,
  ) {
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.spinnerService.show();
    this.candidateEducationService.delete(id).subscribe((response) => {
      this.eventManager.broadcast({
        name: 'candidateEducationListModification',
        content: 'Deleted an candidateEducation'
      });
      this.eventManager.broadcast({name: 'candidateListModification', content: 'OK'});
      this.reloadCandidate();
      this.spinnerService.hide();
      this.activeModal.dismiss(true);
    });
  }

  reloadCandidate() {
    // console.log('Reloading candidate??');
    this.candidateService.getCandidateDetails(this.dataService.getData(USER_ID)).subscribe(
      (res: HttpResponse<Candidate>) => {
        this.dataService.setdata(USER_DATA, JSON.stringify(res.body));
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    return;
  }

  private onError(error: any) {
    this.jhiAlertService.error(error.message, null, null);
  }
}

@Component({
  selector: 'jhi-candidate-education-delete-popup',
  template: ''
})
export class CandidateEducationDeletePopupComponent implements OnInit, OnDestroy {

  routeSub: any;

  constructor(
    private route: ActivatedRoute,
    private candidateEducationPopupService: CandidateEducationPopupService,
    private dataService: DataStorageService
  ) {}

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      if (params['id']) {
        this.candidateEducationPopupService
          .open(CandidateEducationDeleteDialogComponent as Component, params['id']);
      } else {
        const id = this.dataService.getData(CANDIDATE_EDUCATION_ID);
        this.candidateEducationPopupService
          .open(CandidateEducationDeleteDialogComponent as Component, id);

      }
    });
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
  }
}
