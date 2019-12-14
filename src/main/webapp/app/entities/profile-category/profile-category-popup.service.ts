import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { ProfileCategory } from './profile-category.model';
import { ProfileCategoryService } from './profile-category.service';

@Injectable()
export class ProfileCategoryPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(private modalService: NgbModal, private router: Router, private profileCategoryService: ProfileCategoryService) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.profileCategoryService.find(id).subscribe((profileCategoryResponse: HttpResponse<ProfileCategory>) => {
                    const profileCategory: ProfileCategory = profileCategoryResponse.body;
                    this.ngbModalRef = this.profileCategoryModalRef(component, profileCategory);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.profileCategoryModalRef(component, new ProfileCategory());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    profileCategoryModalRef(component: Component, profileCategory: ProfileCategory): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.profileCategory = profileCategory;
        modalRef.result.then(
            result => {
                this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalRef = null;
            },
            reason => {
                this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalRef = null;
            }
        );
        return modalRef;
    }
}
