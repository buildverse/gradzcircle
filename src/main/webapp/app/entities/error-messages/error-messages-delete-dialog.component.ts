import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ErrorMessages } from './error-messages.model';
import { ErrorMessagesPopupService } from './error-messages-popup.service';
import { ErrorMessagesService } from './error-messages.service';

@Component({
    selector: 'jhi-error-messages-delete-dialog',
    templateUrl: './error-messages-delete-dialog.component.html'
})
export class ErrorMessagesDeleteDialogComponent {
    errorMessages: ErrorMessages;

    constructor(
        private errorMessagesService: ErrorMessagesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.errorMessagesService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'errorMessagesListModification',
                content: 'Deleted an errorMessages'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-error-messages-delete-popup',
    template: ''
})
export class ErrorMessagesDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private errorMessagesPopupService: ErrorMessagesPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.errorMessagesPopupService.open(ErrorMessagesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
