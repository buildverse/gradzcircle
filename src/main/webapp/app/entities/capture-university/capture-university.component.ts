import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CaptureUniversity } from './capture-university.model';
import { CaptureUniversityService } from './capture-university.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-capture-university',
    templateUrl: './capture-university.component.html'
})
export class CaptureUniversityComponent implements OnInit, OnDestroy {
captureUniversities: CaptureUniversity[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private captureUniversityService: CaptureUniversityService,
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
            this.captureUniversityService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<CaptureUniversity[]>) => this.captureUniversities = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.captureUniversityService.query().subscribe(
            (res: HttpResponse<CaptureUniversity[]>) => {
                this.captureUniversities = res.body;
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
        this.registerChangeInCaptureUniversities();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CaptureUniversity) {
        return item.id;
    }
    registerChangeInCaptureUniversities() {
        this.eventSubscriber = this.eventManager.subscribe('captureUniversityListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
