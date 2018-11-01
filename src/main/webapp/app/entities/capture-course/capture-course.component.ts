import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CaptureCourse } from './capture-course.model';
import { CaptureCourseService } from './capture-course.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-capture-course',
    templateUrl: './capture-course.component.html'
})
export class CaptureCourseComponent implements OnInit, OnDestroy {
captureCourses: CaptureCourse[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private captureCourseService: CaptureCourseService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.captureCourseService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<CaptureCourse[]>) => this.captureCourses = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.captureCourseService.query().subscribe(
            (res: HttpResponse<CaptureCourse[]>) => {
                this.captureCourses = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInCaptureCourses();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CaptureCourse) {
        return item.id;
    }
    registerChangeInCaptureCourses() {
        this.eventSubscriber = this.eventManager.subscribe('captureCourseListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
