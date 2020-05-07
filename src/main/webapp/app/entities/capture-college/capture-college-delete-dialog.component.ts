import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CaptureCollege } from './capture-college.model';
import { CaptureCollegePopupService } from './capture-college-popup.service';
import { CaptureCollegeService } from './capture-college.service';

@Component({
    selector: 'jhi-capture-college-delete-dialog',
    templateUrl: './capture-college-delete-dialog.component.html'
})
export class CaptureCollegeDeleteDialogComponent {
    captureCollege: CaptureCollege;

    constructor(
        private captureCollegeService: CaptureCollegeService,
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
        this.captureCollegeService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'captureCollegeListModification',
                content: 'Deleted an captureCollege'
            });
            this.clearRoute();
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-capture-college-delete-popup',
    template: ''
})
export class CaptureCollegeDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private captureCollegePopupService: CaptureCollegePopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.captureCollegePopupService.open(CaptureCollegeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
