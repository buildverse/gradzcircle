import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Course } from './course.model';
import { CoursePopupService } from './course-popup.service';
import { CourseService } from './course.service';

@Component({
    selector: 'jhi-course-delete-dialog',
    templateUrl: './course-delete-dialog.component.html'
})
export class CourseDeleteDialogComponent {
    course: Course;

    constructor(
        private courseService: CourseService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router
    ) {}

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.courseService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'courseListModification',
                content: 'Deleted an course'
            });
            this.clearRoute();
            this.activeModal.dismiss(true);
        });
    }
    clearRoute() {
        this.router.navigate(['/admin', { outlets: { popup: null } }]);
    }
}

@Component({
    selector: 'jhi-course-delete-popup',
    template: ''
})
export class CourseDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private coursePopupService: CoursePopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.coursePopupService.open(CourseDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
