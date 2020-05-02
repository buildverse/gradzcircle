import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Job } from '../../shared/job-common/job.model';
import { JobPopupService } from './job-popup.service';
import { JobService } from './job.service';

@Component({
    selector: 'jhi-job-edit-message-dialog',
    templateUrl: './job-edit-message-dialog.html'
})
export class JobEditMessageDialogComponent {
    job: Job;
    constructor(
        // private jobService: JobService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router
    ) {}

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    clearRoute() {
        this.router.navigate(['/', 'corp', { outlets: { popup: null } }]);
    }
}

@Component({
    selector: 'jhi-job-edit-message-popup',
    template: ''
})
export class JobEditMessagePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private jobPopupService: JobPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.jobPopupService.open(JobEditMessageDialogComponent as Component);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
