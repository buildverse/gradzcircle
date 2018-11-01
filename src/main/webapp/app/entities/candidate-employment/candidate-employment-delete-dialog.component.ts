import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

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

        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.candidateEmploymentService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'candidateEmploymentListModification',
                content: 'Deleted an candidateEmployment'
            });
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
        private candidateEmploymentPopupService: CandidateEmploymentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.candidateEmploymentPopupService
                .open(CandidateEmploymentDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
