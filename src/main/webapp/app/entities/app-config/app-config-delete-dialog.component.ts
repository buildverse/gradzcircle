import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AppConfig } from './app-config.model';
import { AppConfigPopupService } from './app-config-popup.service';
import { AppConfigService } from './app-config.service';

@Component({
    selector: 'jhi-app-config-delete-dialog',
    templateUrl: './app-config-delete-dialog.component.html'
})
export class AppConfigDeleteDialogComponent {

    appConfig: AppConfig;

    constructor(
        private appConfigService: AppConfigService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.appConfigService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'appConfigListModification',
                content: 'Deleted an appConfig'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-app-config-delete-popup',
    template: ''
})
export class AppConfigDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private appConfigPopupService: AppConfigPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.appConfigPopupService
                .open(AppConfigDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
