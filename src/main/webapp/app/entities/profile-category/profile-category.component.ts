import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ProfileCategory } from './profile-category.model';
import { ProfileCategoryService } from './profile-category.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-profile-category',
    templateUrl: './profile-category.component.html'
})
export class ProfileCategoryComponent implements OnInit, OnDestroy {
profileCategories: ProfileCategory[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private profileCategoryService: ProfileCategoryService,
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
            this.profileCategoryService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<ProfileCategory[]>) => this.profileCategories = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.profileCategoryService.query().subscribe(
            (res: HttpResponse<ProfileCategory[]>) => {
                this.profileCategories = res.body;
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
        this.registerChangeInProfileCategories();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ProfileCategory) {
        return item.id;
    }
    registerChangeInProfileCategories() {
        this.eventSubscriber = this.eventManager.subscribe('profileCategoryListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
