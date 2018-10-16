import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CandidateJob } from '../candidate-job/candidate-job.model';
import { JobPopupService } from '../job/job-popup.service';
import { JobService } from '../job/job.service';

@Component({
    selector: 'jhi-candidate-list-dialog',
    templateUrl: './candidate-list-dialog.html'
})
export class CandidateListDialogComponent {

    candidateJob: CandidateJob[];
    
    constructor(
       // private jobService: JobService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }
}

@Component({
    selector: 'jhi-candidate-list-popup',
    template: ''
})
export class CandidateListPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobPopupService: JobPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.jobPopupService
                .open(CandidateListPopupComponent as Component);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
