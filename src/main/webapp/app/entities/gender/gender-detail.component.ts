import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Gender } from './gender.model';
import { GenderService } from './gender.service';

@Component({
    selector: 'jhi-gender-detail',
    templateUrl: './gender-detail.component.html'
})
export class GenderDetailComponent implements OnInit, OnDestroy {

    gender: Gender;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private genderService: GenderService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInGenders();
    }

    load(id) {
        this.genderService.find(id).subscribe((gender) => {
            this.gender = gender;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInGenders() {
        this.eventSubscriber = this.eventManager.subscribe(
            'genderListModification',
            (response) => this.load(this.gender.id)
        );
    }
}
