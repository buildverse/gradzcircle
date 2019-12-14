import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { ProfileCategory } from './profile-category.model';
import { ProfileCategoryService } from './profile-category.service';

@Component({
    selector: 'jhi-profile-category-detail',
    templateUrl: './profile-category-detail.component.html'
})
export class ProfileCategoryDetailComponent implements OnInit, OnDestroy {
    profileCategory: ProfileCategory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private profileCategoryService: ProfileCategoryService,
        private route: ActivatedRoute
    ) {}

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerChangeInProfileCategories();
    }

    load(id) {
        this.profileCategoryService.find(id).subscribe((profileCategoryResponse: HttpResponse<ProfileCategory>) => {
            this.profileCategory = profileCategoryResponse.body;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProfileCategories() {
        this.eventSubscriber = this.eventManager.subscribe('profileCategoryListModification', response =>
            this.load(this.profileCategory.id)
        );
    }
}
