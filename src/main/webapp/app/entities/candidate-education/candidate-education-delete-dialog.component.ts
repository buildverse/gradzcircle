import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

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
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.candidateEducationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'candidateEducationListModification',
                content: 'Deleted an candidateEducation'
            });
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
        private candidateEducationPopupService: CandidateEducationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.candidateEducationPopupService
                .open(CandidateEducationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
