import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { CANDIDATE_EMPLOYMENT_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { CandidateEmployment } from './candidate-employment.model';
import { CandidateEmploymentPopupService } from './candidate-employment-popup.service';
import { CandidateEmploymentService } from './candidate-employment.service';

@Component({
    selector: 'jhi-candidate-employment-delete-dialog',
    templateUrl: './candidate-employment-delete-dialog.component.html'
})
export class CandidateEmploymentDeleteDialogComponent {
    candidateEmployment: CandidateEmployment;

    constructor(
        private candidateEmploymentService: CandidateEmploymentService,
        public activeModal: NgbActiveModal,
        private spinnerService: NgxSpinnerService,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.spinnerService.show();
        this.candidateEmploymentService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'candidateEmploymentListModification',
                content: 'Deleted an candidateEmployment'
            });
            this.eventManager.broadcast({ name: 'candidateListModification', content: 'OK' });
            this.spinnerService.hide();
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-candidate-employment-delete-popup',
    template: ''
})
export class CandidateEmploymentDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateEmploymentPopupService: CandidateEmploymentPopupService,
        private dataService: DataStorageService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.candidateEmploymentPopupService.open(CandidateEmploymentDeleteDialogComponent as Component, params['id']);
            } else {
                const id = this.dataService.getData(CANDIDATE_EMPLOYMENT_ID);
                this.candidateEmploymentPopupService.open(CandidateEmploymentDeleteDialogComponent as Component, id);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
