import { CANDIDATE_PROJECT_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
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
    isEmploymentProject: boolean;

    constructor(
        private candidateProjectService: CandidateProjectService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router,
        private activatedRoute: ActivatedRoute
    ) {}

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.candidateProjectService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'candidateProjectListModification',
                content: 'Deleted an candidateProject'
            });
            this.clearRoute();
            this.activeModal.dismiss(true);
        });
    }

    clearRoute() {
        if (this.isEmploymentProject) {
            this.router.navigate(['/employment', { outlets: { popup: null } }]);
        } else {
            this.router.navigate(['/education', { outlets: { popup: null } }]);
        }
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
        private candidateProjectPopupService: CandidateProjectPopupService,
        private dataStorageService: DataStorageService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.candidateProjectPopupService.open(CandidateProjectDeleteDialogComponent as Component, params['id']);
            } else if (this.dataStorageService.getData(CANDIDATE_PROJECT_ID)) {
                this.candidateProjectPopupService.open(
                    CandidateProjectDeleteDialogComponent as Component,
                    this.dataStorageService.getData(CANDIDATE_PROJECT_ID),
                    params['isEmployment']
                );
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
