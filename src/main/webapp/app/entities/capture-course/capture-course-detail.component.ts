import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { CaptureCourse } from './capture-course.model';
import { CaptureCourseService } from './capture-course.service';

@Component({
    selector: 'jhi-capture-course-detail',
    templateUrl: './capture-course-detail.component.html'
})
export class CaptureCourseDetailComponent implements OnInit, OnDestroy {

    captureCourse: CaptureCourse;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private captureCourseService: CaptureCourseService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCaptureCourses();
    }

    load(id) {
        this.captureCourseService.find(id).subscribe((captureCourse) => {
            this.captureCourse = captureCourse;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCaptureCourses() {
        this.eventSubscriber = this.eventManager.subscribe(
            'captureCourseListModification',
            (response) => this.load(this.captureCourse.id)
        );
    }
}
