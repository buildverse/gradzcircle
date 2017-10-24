import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CandidateProject } from './candidate-project.model';
import { CandidateProjectPopupService } from './candidate-project-popup.service';
import { CandidateProjectService } from './candidate-project.service';

@Component({
    selector: 'jhi-candidate-project-delete-dialog',
    templateUrl: './candidate-project-delete-dialog.component.html'
})
export class CandidateProjectDeleteDialogComponent {

    candidateProject: CandidateProject;

    constructor(
        private candidateProjectService: CandidateProjectService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.candidateProjectService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'candidateProjectListModification',
                content: 'Deleted an candidateProject'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-candidate-project-delete-popup',
    template: ''
})
export class CandidateProjectDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateProjectPopupService: CandidateProjectPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.candidateProjectPopupService
                .open(CandidateProjectDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
