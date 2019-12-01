import { Principal } from '../../core/auth/principal.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Nationality } from './nationality.model';
import { NationalityService } from './nationality.service';

@Component({
    selector: 'jhi-nationality',
    templateUrl: './nationality.component.html'
})
export class NationalityComponent implements OnInit, OnDestroy {
    nationalities: Nationality[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private nationalityService: NationalityService,
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
            this.nationalityService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<Nationality[]>) => (this.nationalities = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.nationalityService.query().subscribe(
            (res: HttpResponse<Nationality[]>) => {
                this.nationalities = res.body;
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
        this.registerChangeInNationalities();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Nationality) {
        return item.id;
    }
    registerChangeInNationalities() {
        this.eventSubscriber = this.eventManager.subscribe('nationalityListModification', response => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
