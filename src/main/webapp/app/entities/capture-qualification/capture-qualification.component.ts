import { Principal } from '../../core/auth/principal.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CaptureQualification } from './capture-qualification.model';
import { CaptureQualificationService } from './capture-qualification.service';

@Component({
    selector: 'jhi-capture-qualification',
    templateUrl: './capture-qualification.component.html'
})
export class CaptureQualificationComponent implements OnInit, OnDestroy {
    captureQualifications: CaptureQualification[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private captureQualificationService: CaptureQualificationService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.captureQualificationService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<CaptureQualification[]>) => (this.captureQualifications = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.captureQualificationService.query().subscribe(
            (res: HttpResponse<CaptureQualification[]>) => {
                this.captureQualifications = res.body;
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
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInCaptureQualifications();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CaptureQualification) {
        return item.id;
    }
    registerChangeInCaptureQualifications() {
        this.eventSubscriber = this.eventManager.subscribe('captureQualificationListModification', response => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
