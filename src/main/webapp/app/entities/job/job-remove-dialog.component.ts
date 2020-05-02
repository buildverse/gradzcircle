import { JOB_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { Job } from '../../shared/job-common/job.model';
import { JobPopupService } from './job-popup.service';
import { JobService } from './job.service';

@Component({
    selector: 'jhi-job-delete-dialog',
    templateUrl: './job-delete-dialog.component.html'
})
export class JobRemoveDialogComponent {
    job: Job;

    constructor(
        private jobService: JobService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router
    ) {}

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.jobService.remove(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'jobListModification',
                content: 'Deleted an job'
            });
            this.clearRoute();
            this.activeModal.dismiss(true);
        });
    }
    clearRoute() {
        this.router.navigate(['/', 'corp', { outlets: { popup: null } }]);
    }
}

@Component({
    selector: 'jhi-job-delete-popup',
    template: ''
})
export class JobRemovePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private jobPopupService: JobPopupService, private dataService: DataStorageService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.jobPopupService.open(JobRemoveDialogComponent as Component, params['id']);
            } else {
                this.jobPopupService.open(JobRemoveDialogComponent as Component, this.dataService.getData(JOB_ID));
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
