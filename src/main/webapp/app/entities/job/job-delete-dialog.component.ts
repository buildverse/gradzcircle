import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { NgxSpinnerService } from 'ngx-spinner';
import { Job } from '../../shared/job-common/job.model';
import { JobPopupService } from './job-popup.service';
import { JobService } from './job.service';

@Component({
    selector: 'jhi-job-delete-dialog',
    templateUrl: './job-delete-dialog.component.html'
})
export class JobDeleteDialogComponent {
    job: Job;

    constructor(
        private jobService: JobService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private spinnerService: NgxSpinnerService
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.spinnerService.show();
        this.jobService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'jobListModification',
                content: 'Deleted an job'
            });
            this.spinnerService.hide();
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-job-delete-popup',
    template: ''
})
export class JobDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private jobPopupService: JobPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.jobPopupService.open(JobDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
