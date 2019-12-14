import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { ErrorMessages } from './error-messages.model';
import { ErrorMessagesService } from './error-messages.service';

@Component({
    selector: 'jhi-error-messages-detail',
    templateUrl: './error-messages-detail.component.html'
})
export class ErrorMessagesDetailComponent implements OnInit, OnDestroy {
    errorMessages: ErrorMessages;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(private eventManager: JhiEventManager, private errorMessagesService: ErrorMessagesService, private route: ActivatedRoute) {}

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerChangeInErrorMessages();
    }

    load(id) {
        this.errorMessagesService.find(id).subscribe((errorMessagesResponse: HttpResponse<ErrorMessages>) => {
            this.errorMessages = errorMessagesResponse.body;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInErrorMessages() {
        this.eventSubscriber = this.eventManager.subscribe('errorMessagesListModification', response => this.load(this.errorMessages.id));
    }
}
