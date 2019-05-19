import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {DataStorageService} from '../../shared';
import { CANDIDATE_EDUCATION_ID } from '../../shared/constants/storage.constants';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import {NgxSpinnerService} from 'ngx-spinner';
import { CandidateEducation } from './candidate-education.model';
import { CandidateEducationPopupService } from './candidate-education-popup.service';
import { CandidateEducationService } from './candidate-education.service';

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
        private spinnerService: NgxSpinnerService
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
        this.spinnerService.hide();
        this.activeModal.dismiss(true);
      });
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
          if(params['id']) {
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
