import { DataStorageService } from '../../shared';
import { CANDIDATE_ID, CANDIDATE_NON_ACADEMIC_ID } from '../../shared/constants/storage.constants';
import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';
import {CandidateNonAcademicWork} from './candidate-non-academic-work.model';
import {CandidateNonAcademicWorkPopupService} from './candidate-non-academic-work-popup.service';
import {CandidateNonAcademicWorkService} from './candidate-non-academic-work.service';

@Component({
  selector: 'jhi-candidate-non-academic-work-delete-dialog',
  templateUrl: './candidate-non-academic-work-delete-dialog.component.html'
})
export class CandidateNonAcademicWorkDeleteDialogComponent {

  candidateNonAcademicWork: CandidateNonAcademicWork;

  constructor(
    private candidateNonAcademicWorkService: CandidateNonAcademicWorkService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    private spinnerService: NgxSpinnerService
  ) {
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.spinnerService.show();
    this.candidateNonAcademicWorkService.delete(id).subscribe((response) => {
      this.eventManager.broadcast({
        name: 'candidateNonAcademicWorkListModification',
        content: 'Deleted an candidateNonAcademicWork'
      });
       this.eventManager.broadcast({name: 'candidateListModification', content: 'OK'});
      this.activeModal.dismiss(true);
      this.spinnerService.hide();
    });
  }
}

@Component({
  selector: 'jhi-candidate-non-academic-work-delete-popup',
  template: ''
})
export class CandidateNonAcademicWorkDeletePopupComponent implements OnInit, OnDestroy {

  routeSub: any;

  constructor(
    private route: ActivatedRoute,
    private candidateNonAcademicWorkPopupService: CandidateNonAcademicWorkPopupService,
    private dataService: DataStorageService
  ) {}

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      if (params['id']) {
        this.candidateNonAcademicWorkPopupService
          .open(CandidateNonAcademicWorkDeleteDialogComponent as Component, params['id']);
      } else {
        const id = this.dataService.getData(CANDIDATE_NON_ACADEMIC_ID);
        this.candidateNonAcademicWorkPopupService
          .open(CandidateNonAcademicWorkDeleteDialogComponent as Component, id);
      }
    });
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
  }
}
