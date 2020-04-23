import { Component, OnInit, OnDestroy } from '@angular/core';

import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { CANDIDATE_LANGUAGE_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
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
        private eventManager: JhiEventManager,
        private spinnerService: NgxSpinnerService,
        private router: Router,
        private activatedRoute: ActivatedRoute
    ) {}

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    clearRoute() {
        this.router.navigate(['/languages', { outlets: { popup: null } }]);
    }

    confirmDelete(id: number) {
        this.spinnerService.show();
        this.candidateLanguageProficiencyService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'candidateLanguageProficiencyListModification',
                content: 'Deleted an candidateLanguageProficiency'
            });
            this.eventManager.broadcast({ name: 'candidateListModification', content: 'OK' });
            this.spinnerService.hide();
            this.clearRoute();
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
        private candidateLanguageProficiencyPopupService: CandidateLanguageProficiencyPopupService,
        private dataService: DataStorageService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.candidateLanguageProficiencyPopupService.open(
                    CandidateLanguageProficiencyDeleteDialogComponent as Component,
                    params['id']
                );
            } else {
                const id = this.dataService.getData(CANDIDATE_LANGUAGE_ID);
                this.candidateLanguageProficiencyPopupService.open(CandidateLanguageProficiencyDeleteDialogComponent as Component, id);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
