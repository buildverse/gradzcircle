import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Candidate } from './candidate.model';
import { CandidatePopupService } from './candidate-popup.service';
import { CandidateService } from './candidate.service';

@Component({
    selector: 'jhi-candidate-delete-dialog',
    templateUrl: './candidate-delete-dialog.component.html'
})
export class CandidateDeleteDialogComponent {

    candidate: Candidate;

    constructor(
        private candidateService: CandidateService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.candidateService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'candidateListModification',
                content: 'Deleted an candidate'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-candidate-delete-popup',
    template: ''
})
export class CandidateDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidatePopupService: CandidatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.candidatePopupService
                .open(CandidateDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
