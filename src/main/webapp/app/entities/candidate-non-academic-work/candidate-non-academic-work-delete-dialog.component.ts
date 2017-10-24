import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CandidateNonAcademicWork } from './candidate-non-academic-work.model';
import { CandidateNonAcademicWorkPopupService } from './candidate-non-academic-work-popup.service';
import { CandidateNonAcademicWorkService } from './candidate-non-academic-work.service';

@Component({
    selector: 'jhi-candidate-non-academic-work-delete-dialog',
    templateUrl: './candidate-non-academic-work-delete-dialog.component.html'
})
export class CandidateNonAcademicWorkDeleteDialogComponent {

    candidateNonAcademicWork: CandidateNonAcademicWork;

    constructor(
        private candidateNonAcademicWorkService: CandidateNonAcademicWorkService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.candidateNonAcademicWorkService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'candidateNonAcademicWorkListModification',
                content: 'Deleted an candidateNonAcademicWork'
            });
            this.activeModal.dismiss(true);
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
        private candidateNonAcademicWorkPopupService: CandidateNonAcademicWorkPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.candidateNonAcademicWorkPopupService
                .open(CandidateNonAcademicWorkDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
