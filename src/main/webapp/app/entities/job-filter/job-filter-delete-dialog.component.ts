import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { JobFilter } from './job-filter.model';
import { JobFilterPopupService } from './job-filter-popup.service';
import { JobFilterService } from './job-filter.service';

@Component({
    selector: 'jhi-job-filter-delete-dialog',
    templateUrl: './job-filter-delete-dialog.component.html'
})
export class JobFilterDeleteDialogComponent {
    jobFilter: JobFilter;

    constructor(
        private jobFilterService: JobFilterService,
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
        this.jobFilterService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'jobFilterListModification',
                content: 'Deleted an jobFilter'
            });
            this.clearRoute();
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-job-filter-delete-popup',
    template: ''
})
export class JobFilterDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private jobFilterPopupService: JobFilterPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.jobFilterPopupService.open(JobFilterDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
