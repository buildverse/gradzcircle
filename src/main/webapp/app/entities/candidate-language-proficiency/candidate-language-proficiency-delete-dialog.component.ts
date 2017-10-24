import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CandidateLanguageProficiency } from './candidate-language-proficiency.model';
import { CandidateLanguageProficiencyPopupService } from './candidate-language-proficiency-popup.service';
import { CandidateLanguageProficiencyService } from './candidate-language-proficiency.service';

@Component({
    selector: 'jhi-candidate-language-proficiency-delete-dialog',
    templateUrl: './candidate-language-proficiency-delete-dialog.component.html'
})
export class CandidateLanguageProficiencyDeleteDialogComponent {

    candidateLanguageProficiency: CandidateLanguageProficiency;

    constructor(
        private candidateLanguageProficiencyService: CandidateLanguageProficiencyService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.candidateLanguageProficiencyService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'candidateLanguageProficiencyListModification',
                content: 'Deleted an candidateLanguageProficiency'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-candidate-language-proficiency-delete-popup',
    template: ''
})
export class CandidateLanguageProficiencyDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateLanguageProficiencyPopupService: CandidateLanguageProficiencyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.candidateLanguageProficiencyPopupService
                .open(CandidateLanguageProficiencyDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
