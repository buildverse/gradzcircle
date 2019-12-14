import { Principal } from '../../core/auth/principal.service';
import { CANDIDATE_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CandidateSkills } from './candidate-skills.model';
import { CandidateSkillsPopupService } from './candidate-skills-popup.service';
import { CandidateSkillsService } from './candidate-skills.service';
import { Candidate, CandidateService } from '../candidate';
import { Skills, SkillsService } from '../skills';
import { CandidateSkillsPopupServiceNew } from './candidate-skills-new-popup.service';

@Component({
    selector: 'jhi-candidate-skills-dialog',
    templateUrl: './candidate-skills-dialog.component.html'
})
export class CandidateSkillsDialogComponent implements OnInit {
    candidateSkills: CandidateSkills;
    isSaving: boolean;
    skillAlreadyPresent: boolean;
    candidates: Candidate[];
    showSkillsTextArea: boolean;
    skills: Skills[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private candidateSkillsService: CandidateSkillsService,
        private candidateService: CandidateService,
        private skillsService: SkillsService,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private skillService: SkillsService
    ) {
        this.skillAlreadyPresent = false;
        this.showSkillsTextArea = false;
    }

    requestSkillData = (text: string): Observable<HttpResponse<any>> => {
        return this.skillService
            .searchRemote({
                query: text
            })
            .map(data => (data.body === '' ? '' : data.body));
    };

    ngOnInit() {
        this.isSaving = false;
        if (this.principal.hasAnyAuthorityDirect(['ROLE_ADMIN'])) {
            this.candidateService.query().subscribe(
                (res: HttpResponse<Candidate[]>) => {
                    this.candidates = res.body;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
            this.skillsService.query().subscribe(
                (res: HttpResponse<Skills[]>) => {
                    this.skills = res.body;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        } else if (this.principal.hasAnyAuthorityDirect(['ROLE_CANDIDATE'])) {
            if (this.candidateSkills.skills) {
                this.candidateSkills.skills.length === 0 ? (this.showSkillsTextArea = true) : (this.showSkillsTextArea = false);
            }
        }
    }

    isSkillOther() {
        this.showSkillsTextArea = false;
        if (this.candidateSkills.skills) {
            this.candidateSkills.skills.forEach(skill => {
                if (skill.value === 'Other') {
                    this.showSkillsTextArea = true;
                }
            });
        }
    }

    isOtherPresent() {
        if (this.candidateSkills.skills) {
            this.candidateSkills.skills.forEach(skill => {
                if (skill.value === 'Other') {
                    this.showSkillsTextArea = true;
                } else {
                    this.showSkillsTextArea = false;
                    this.candidateSkills.capturedSkills = undefined;
                }
            });
            if (this.candidateSkills.skills.length <= 0) {
                this.showSkillsTextArea = false;
                this.candidateSkills.capturedSkills = undefined;
            }
        }
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.candidateSkills.id !== undefined) {
            this.subscribeToSaveResponse(this.candidateSkillsService.update(this.candidateSkills));
        } else {
            this.subscribeToSaveResponse(this.candidateSkillsService.create(this.candidateSkills));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<CandidateSkills>>) {
        result.subscribe(
            (res: HttpResponse<CandidateSkills>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess(result: CandidateSkills) {
        this.eventManager.broadcast({ name: 'candidateSkillsListModification', content: 'OK' });
        this.eventManager.broadcast({ name: 'candidateListModification', content: 'OK' });
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCandidateById(index: number, item: Candidate) {
        return item.id;
    }

    trackSkillsById(index: number, item: Skills) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-candidate-skills-popup',
    template: ''
})
export class CandidateSkillsPopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private candidateSkillsPopupService: CandidateSkillsPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.candidateSkillsPopupService.open(CandidateSkillsDialogComponent as Component, params['id']);
            } else {
                this.candidateSkillsPopupService.open(CandidateSkillsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

@Component({
    selector: 'jhi-candidate-skills-popup',
    template: ''
})
export class CandidateSkillsPopupNewComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateSkillsPopupService: CandidateSkillsPopupServiceNew,
        private dataService: DataStorageService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.candidateSkillsPopupService.open(CandidateSkillsDialogComponent as Component, params['id']);
            } else {
                const id = this.dataService.getData(CANDIDATE_ID);
                this.candidateSkillsPopupService.open(CandidateSkillsDialogComponent as Component, id);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
