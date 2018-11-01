import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CandidateCertification } from './candidate-certification.model';
import { CandidateCertificationPopupService } from './candidate-certification-popup.service';
import { CandidateCertificationService } from './candidate-certification.service';

@Component({
    selector: 'jhi-candidate-certification-delete-dialog',
    templateUrl: './candidate-certification-delete-dialog.component.html'
})
export class CandidateCertificationDeleteDialogComponent {

    candidateCertification: CandidateCertification;

    constructor(
        private candidateCertificationService: CandidateCertificationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.candidateCertificationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'candidateCertificationListModification',
                content: 'Deleted an candidateCertification'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-candidate-certification-delete-popup',
    template: ''
})
export class CandidateCertificationDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateCertificationPopupService: CandidateCertificationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.candidateCertificationPopupService
                .open(CandidateCertificationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
