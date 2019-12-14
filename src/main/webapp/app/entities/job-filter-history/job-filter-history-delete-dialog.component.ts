import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { JobFilterHistory } from './job-filter-history.model';
import { JobFilterHistoryPopupService } from './job-filter-history-popup.service';
import { JobFilterHistoryService } from './job-filter-history.service';

@Component({
    selector: 'jhi-job-filter-history-delete-dialog',
    templateUrl: './job-filter-history-delete-dialog.component.html'
})
export class JobFilterHistoryDeleteDialogComponent {
    jobFilterHistory: JobFilterHistory;

    constructor(
        private jobFilterHistoryService: JobFilterHistoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.jobFilterHistoryService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'jobFilterHistoryListModification',
                content: 'Deleted an jobFilterHistory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-job-filter-history-delete-popup',
    template: ''
})
export class JobFilterHistoryDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private jobFilterHistoryPopupService: JobFilterHistoryPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.jobFilterHistoryPopupService.open(JobFilterHistoryDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
