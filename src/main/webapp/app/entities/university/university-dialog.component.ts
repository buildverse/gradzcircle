import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { University } from './university.model';
import { UniversityPopupService } from './university-popup.service';
import { UniversityService } from './university.service';
import { Country, CountryService } from '../country';

@Component({
    selector: 'jhi-university-dialog',
    templateUrl: './university-dialog.component.html'
})
export class UniversityDialogComponent implements OnInit {
    university: University;
    isSaving: boolean;

    countries: Country[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private universityService: UniversityService,
        private countryService: CountryService,
        private eventManager: JhiEventManager,
        private router: Router
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.countryService.query().subscribe(
            (res: HttpResponse<Country[]>) => {
                this.countries = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    clear() {
        this.clearRoute();
        this.activeModal.dismiss('cancel');
    }

    clearRoute() {
        this.router.navigate(['/admin', { outlets: { popup: null } }]);
    }

    save() {
        this.isSaving = true;
        if (this.university.id !== undefined) {
            this.subscribeToSaveResponse(this.universityService.update(this.university));
        } else {
            this.subscribeToSaveResponse(this.universityService.create(this.university));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<University>>) {
        result.subscribe((res: HttpResponse<University>) => this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: University) {
        this.eventManager.broadcast({ name: 'universityListModification', content: 'OK' });
        this.isSaving = false;
        this.clearRoute();
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCountryById(index: number, item: Country) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-university-popup',
    template: ''
})
export class UniversityPopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(private route: ActivatedRoute, private universityPopupService: UniversityPopupService) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                this.universityPopupService.open(UniversityDialogComponent as Component, params['id']);
            } else {
                this.universityPopupService.open(UniversityDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
