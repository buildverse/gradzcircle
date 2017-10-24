import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Qualification } from './qualification.model';
import { QualificationService } from './qualification.service';

@Component({
    selector: 'jhi-qualification-detail',
    templateUrl: './qualification-detail.component.html'
})
export class QualificationDetailComponent implements OnInit, OnDestroy {

    qualification: Qualification;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private qualificationService: QualificationService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInQualifications();
    }

    load(id) {
        this.qualificationService.find(id).subscribe((qualification) => {
            this.qualification = qualification;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInQualifications() {
        this.eventSubscriber = this.eventManager.subscribe(
            'qualificationListModification',
            (response) => this.load(this.qualification.id)
        );
    }
}
