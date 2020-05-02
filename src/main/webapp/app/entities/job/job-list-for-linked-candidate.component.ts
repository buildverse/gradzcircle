import { CANDIDATE_ID, CORPORATE_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { JobListPopupService } from './job-list-popup.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-job-list-linked-candidates-popup',
    templateUrl: './job-list-linked-candidate.html'
})
export class JobListForLinkedCandidateComponent {
    jobList: JobListForLinkedCandidateComponent[];
    constructor(
        // private jobService: JobService,
        private activeModal: NgbActiveModal,
        private router: Router
    ) {}

    clear() {
        this.router.navigate(['/', 'corp', { outlets: { popup: null } }]);
        this.activeModal.dismiss('cancel');
    }
}

@Component({
    selector: 'jhi-job-list-linked-candidates-popup',
    template: ''
})
export class JobListForLinkedCandidatePopUpComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobPopupService: JobListPopupService,
        private localStorageService: DataStorageService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            const corporateId = this.localStorageService.getData(CORPORATE_ID);
            const candidateId = this.localStorageService.getData(CANDIDATE_ID);
            this.jobPopupService.open(JobListForLinkedCandidateComponent as Component, candidateId, corporateId);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
