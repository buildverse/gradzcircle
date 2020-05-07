import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Qualification } from './qualification.model';
import { QualificationPopupService } from './qualification-popup.service';
import { QualificationService } from './qualification.service';

@Component({
    selector: 'jhi-qualification-delete-dialog',
    templateUrl: './qualification-delete-dialog.component.html'
})
export class QualificationDeleteDialogComponent {
    qualification: Qualification;

    constructor(
        private qualificationService: QualificationService,
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
        this.qualificationService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'qualificationListModification',
                content: 'Deleted an qualification'
            });
            this.clearRoute();
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-qualification-delete-popup',
    template: ''
})
export class QualificationDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private qualificationPopupService: QualificationPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.qualificationPopupService.open(QualificationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
