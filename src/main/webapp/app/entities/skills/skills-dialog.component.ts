import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

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
        private jhiAlertService: JhiAlertService,
        private skillsService: SkillsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.skills.id !== undefined) {
            this.subscribeToSaveResponse(
                this.skillsService.update(this.skills));
        } else {
            this.subscribeToSaveResponse(
                this.skillsService.create(this.skills));
        }
    }

    private subscribeToSaveResponse(result: Observable<Skills>) {
        result.subscribe((res: Skills) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Skills) {
        this.eventManager.broadcast({ name: 'skillsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-skills-popup',
    template: ''
})
export class SkillsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private skillsPopupService: SkillsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.skillsPopupService
                    .open(SkillsDialogComponent as Component, params['id']);
            } else {
                this.skillsPopupService
                    .open(SkillsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
