import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Skills } from './skills.model';
import { SkillsPopupService } from './skills-popup.service';
import { SkillsService } from './skills.service';

@Component({
    selector: 'jhi-skills-delete-dialog',
    templateUrl: './skills-delete-dialog.component.html'
})
export class SkillsDeleteDialogComponent {
    skills: Skills;

    constructor(
        private skillsService: SkillsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router
    ) {}

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    clearRoute() {
        this.router.navigate(['/admin', { outlets: { popup: null } }]);
    }

    confirmDelete(id: number) {
        this.skillsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'skillsListModification',
                content: 'Deleted an skills'
            });
            this.clearRoute();
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-skills-delete-popup',
    template: ''
})
export class SkillsDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private skillsPopupService: SkillsPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.skillsPopupService.open(SkillsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
