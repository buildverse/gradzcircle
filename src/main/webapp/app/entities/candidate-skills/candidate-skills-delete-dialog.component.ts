import { CANDIDATE_SKILL_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CandidateSkills } from './candidate-skills.model';
import { CandidateSkillsPopupService } from './candidate-skills-popup.service';
import { CandidateSkillsService } from './candidate-skills.service';

@Component({
    selector: 'jhi-candidate-skills-delete-dialog',
    templateUrl: './candidate-skills-delete-dialog.component.html'
})
export class CandidateSkillsDeleteDialogComponent {
    candidateSkills: CandidateSkills;

    constructor(
        private candidateSkillsService: CandidateSkillsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router,
        private activatedRoute: ActivatedRoute
    ) {}

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }
    clearRoute() {
        this.router.navigate(['/skill', { outlets: { popup: null } }]);
    }

    confirmDelete(id: number) {
        this.candidateSkillsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'candidateSkillsListModification',
                content: 'Deleted an candidateSkills'
            });
            this.eventManager.broadcast({ name: 'candidateListModification', content: 'OK' });
            this.clearRoute();
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-candidate-skills-delete-popup',
    template: ''
})
export class CandidateSkillsDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateSkillsPopupService: CandidateSkillsPopupService,
        private dataService: DataStorageService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id]']) {
                this.candidateSkillsPopupService.open(CandidateSkillsDeleteDialogComponent as Component, params['id']);
            } else {
                const id = this.dataService.getData(CANDIDATE_SKILL_ID);
                this.candidateSkillsPopupService.open(CandidateSkillsDeleteDialogComponent as Component, id);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
