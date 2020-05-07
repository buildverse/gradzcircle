import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Skills } from './skills.model';
import { SkillsPopupService } from './skills-popup.service';
import { SkillsService } from './skills.service';

@Component({
    selector: 'jhi-skills-dialog',
    templateUrl: './skills-dialog.component.html'
})
export class SkillsDialogComponent implements OnInit {
    skills: Skills;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private skillsService: SkillsService,
        private eventManager: JhiEventManager,
        private router: Router
    ) {}

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }
    clearRoute() {
        this.router.navigate(['/admin', { outlets: { popup: null } }]);
    }

    save() {
        this.isSaving = true;
        if (this.skills.id !== undefined) {
            this.subscribeToSaveResponse(this.skillsService.update(this.skills));
        } else {
            this.subscribeToSaveResponse(this.skillsService.create(this.skills));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Skills>>) {
        result.subscribe((res: HttpResponse<Skills>) => this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Skills) {
        this.eventManager.broadcast({ name: 'skillsListModification', content: 'OK' });
        this.isSaving = false;
        this.clearRoute();
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-skills-popup',
    template: ''
})
export class SkillsPopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private skillsPopupService: SkillsPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.skillsPopupService.open(SkillsDialogComponent as Component, params['id']);
            } else {
                this.skillsPopupService.open(SkillsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
