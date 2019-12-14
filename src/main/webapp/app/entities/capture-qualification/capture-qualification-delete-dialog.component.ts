import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CaptureQualification } from './capture-qualification.model';
import { CaptureQualificationPopupService } from './capture-qualification-popup.service';
import { CaptureQualificationService } from './capture-qualification.service';

@Component({
    selector: 'jhi-capture-qualification-delete-dialog',
    templateUrl: './capture-qualification-delete-dialog.component.html'
})
export class CaptureQualificationDeleteDialogComponent {
    captureQualification: CaptureQualification;

    constructor(
        private captureQualificationService: CaptureQualificationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.captureQualificationService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'captureQualificationListModification',
                content: 'Deleted an captureQualification'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-capture-qualification-delete-popup',
    template: ''
})
export class CaptureQualificationDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private captureQualificationPopupService: CaptureQualificationPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.captureQualificationPopupService.open(CaptureQualificationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
