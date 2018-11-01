import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CaptureCollege } from './capture-college.model';
import { CaptureCollegeService } from './capture-college.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-capture-college',
    templateUrl: './capture-college.component.html'
})
export class CaptureCollegeComponent implements OnInit, OnDestroy {
captureColleges: CaptureCollege[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private captureCollegeService: CaptureCollegeService,
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
            this.captureCollegeService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<CaptureCollege[]>) => this.captureColleges = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.captureCollegeService.query().subscribe(
            (res: HttpResponse<CaptureCollege[]>) => {
                this.captureColleges = res.body;
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
        this.registerChangeInCaptureColleges();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CaptureCollege) {
        return item.id;
    }
    registerChangeInCaptureColleges() {
        this.eventSubscriber = this.eventManager.subscribe('captureCollegeListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
