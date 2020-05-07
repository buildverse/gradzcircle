import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CaptureUniversity } from './capture-university.model';
import { CaptureUniversityPopupService } from './capture-university-popup.service';
import { CaptureUniversityService } from './capture-university.service';

@Component({
    selector: 'jhi-capture-university-delete-dialog',
    templateUrl: './capture-university-delete-dialog.component.html'
})
export class CaptureUniversityDeleteDialogComponent {
    captureUniversity: CaptureUniversity;

    constructor(
        private captureUniversityService: CaptureUniversityService,
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
        this.captureUniversityService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'captureUniversityListModification',
                content: 'Deleted an captureUniversity'
            });
            this.clearRoute();
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-capture-university-delete-popup',
    template: ''
})
export class CaptureUniversityDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private captureUniversityPopupService: CaptureUniversityPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.captureUniversityPopupService.open(CaptureUniversityDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
