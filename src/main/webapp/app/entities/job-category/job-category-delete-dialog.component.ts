import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { JobCategory } from './job-category.model';
import { JobCategoryPopupService } from './job-category-popup.service';
import { JobCategoryService } from './job-category.service';

@Component({
    selector: 'jhi-job-category-delete-dialog',
    templateUrl: './job-category-delete-dialog.component.html'
})
export class JobCategoryDeleteDialogComponent {
    jobCategory: JobCategory;

    constructor(
        private jobCategoryService: JobCategoryService,
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
        this.jobCategoryService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'jobCategoryListModification',
                content: 'Deleted an jobCategory'
            });
            this.clearRoute();
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-job-category-delete-popup',
    template: ''
})
export class JobCategoryDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private jobCategoryPopupService: JobCategoryPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.jobCategoryPopupService.open(JobCategoryDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
