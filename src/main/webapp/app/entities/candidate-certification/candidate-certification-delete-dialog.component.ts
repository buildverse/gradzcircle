import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { CANDIDATE_CERTIFICATION_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
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
        private eventManager: JhiEventManager,
        private router: Router,
        private activatedRoute: ActivatedRoute
    ) {}

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    clearRoute() {
        this.router.navigate(['/certification', { outlets: { popup: null } }]);
    }
    confirmDelete(id: number) {
        this.candidateCertificationService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'candidateCertificationListModification',
                content: 'Deleted an candidateCertification'
            });
            this.eventManager.broadcast({ name: 'candidateListModification', content: 'OK' });
            this.clearRoute();
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
        private candidateCertificationPopupService: CandidateCertificationPopupService,
        private dataService: DataStorageService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.candidateCertificationPopupService.open(CandidateCertificationDeleteDialogComponent as Component, params['id']);
            } else {
                const id = this.dataService.getData(CANDIDATE_CERTIFICATION_ID);
                this.candidateCertificationPopupService.open(CandidateCertificationDeleteDialogComponent as Component, id);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
