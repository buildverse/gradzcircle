import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CANDIDATE_EDUCATION_ID, USER_ID, USER_DATA, HAS_EDUCATION } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { Candidate } from '../candidate';
import { CandidateService } from '../candidate/candidate.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { NgxSpinnerService } from 'ngx-spinner';
import { CandidateEducation } from './candidate-education.model';
import { CandidateEducationPopupService } from './candidate-education-popup.service';
import { CandidateEducationService } from './candidate-education.service';
import { HttpErrorResponse } from '@angular/common/http';
import { HttpResponse } from '@angular/common/http';

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
        private activatedRoute: ActivatedRoute,
        private router: Router
    ) {}

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.spinnerService.show();
        this.candidateEducationService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'candidateEducationListModification',
                content: 'Deleted an candidateEducation'
            });
            this.eventManager.broadcast({ name: 'candidateListModification', content: 'OK' });
            this.spinnerService.hide();
            this.clearRoute();
            this.activeModal.dismiss(true);
        });
    }

    clearRoute() {
        this.router.navigate(['/education', { outlets: { popup: null } }]);
    }
    private onError(error: any) {
        this.router.navigate(['/error']);
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
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.candidateEducationPopupService.open(CandidateEducationDeleteDialogComponent as Component, params['id']);
            } else {
                const id = this.dataService.getData(CANDIDATE_EDUCATION_ID);
                this.candidateEducationPopupService.open(CandidateEducationDeleteDialogComponent as Component, id);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
