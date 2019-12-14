import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { States } from './states.model';
import { StatesPopupService } from './states-popup.service';
import { StatesService } from './states.service';

@Component({
    selector: 'jhi-states-delete-dialog',
    templateUrl: './states-delete-dialog.component.html'
})
export class StatesDeleteDialogComponent {
    states: States;

    constructor(private statesService: StatesService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.statesService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'statesListModification',
                content: 'Deleted an states'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-states-delete-popup',
    template: ''
})
export class StatesDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private statesPopupService: StatesPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.statesPopupService.open(StatesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
