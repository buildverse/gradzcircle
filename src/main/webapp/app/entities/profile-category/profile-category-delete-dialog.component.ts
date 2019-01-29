import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProfileCategory } from './profile-category.model';
import { ProfileCategoryPopupService } from './profile-category-popup.service';
import { ProfileCategoryService } from './profile-category.service';

@Component({
    selector: 'jhi-profile-category-delete-dialog',
    templateUrl: './profile-category-delete-dialog.component.html'
})
export class ProfileCategoryDeleteDialogComponent {

    profileCategory: ProfileCategory;

    constructor(
        private profileCategoryService: ProfileCategoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.profileCategoryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'profileCategoryListModification',
                content: 'Deleted an profileCategory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-profile-category-delete-popup',
    template: ''
})
export class ProfileCategoryDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private profileCategoryPopupService: ProfileCategoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.profileCategoryPopupService
                .open(ProfileCategoryDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
