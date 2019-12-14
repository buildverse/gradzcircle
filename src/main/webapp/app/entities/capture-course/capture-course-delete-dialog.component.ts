import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CaptureCourse } from './capture-course.model';
import { CaptureCoursePopupService } from './capture-course-popup.service';
import { CaptureCourseService } from './capture-course.service';

@Component({
    selector: 'jhi-capture-course-delete-dialog',
    templateUrl: './capture-course-delete-dialog.component.html'
})
export class CaptureCourseDeleteDialogComponent {
    captureCourse: CaptureCourse;

    constructor(
        private captureCourseService: CaptureCourseService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.captureCourseService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'captureCourseListModification',
                content: 'Deleted an captureCourse'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-capture-course-delete-popup',
    template: ''
})
export class CaptureCourseDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private captureCoursePopupService: CaptureCoursePopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.captureCoursePopupService.open(CaptureCourseDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
