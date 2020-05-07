import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Country } from './country.model';
import { CountryPopupService } from './country-popup.service';
import { CountryService } from './country.service';

@Component({
    selector: 'jhi-country-delete-dialog',
    templateUrl: './country-delete-dialog.component.html'
})
export class CountryDeleteDialogComponent {
    country: Country;

    constructor(
        private countryService: CountryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router
    ) {}

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.countryService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'countryListModification',
                content: 'Deleted an country'
            });
            this.clearRoute();
            this.activeModal.dismiss(true);
        });
    }

    clearRoute() {
        this.router.navigate(['/admin', { outlets: { popup: null } }]);
    }
}

@Component({
    selector: 'jhi-country-delete-popup',
    template: ''
})
export class CountryDeletePopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private countryPopupService: CountryPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.countryPopupService.open(CountryDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
