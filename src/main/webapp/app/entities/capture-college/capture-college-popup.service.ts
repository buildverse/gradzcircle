import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { CaptureCollege } from './capture-college.model';
import { CaptureCollegeService } from './capture-college.service';

@Injectable()
export class CaptureCollegePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private captureCollegeService: CaptureCollegeService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.captureCollegeService.find(id)
                    .subscribe((captureCollegeResponse: HttpResponse<CaptureCollege>) => {
                        const captureCollege: CaptureCollege = captureCollegeResponse.body;
                        this.ngbModalRef = this.captureCollegeModalRef(component, captureCollege);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.captureCollegeModalRef(component, new CaptureCollege());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    captureCollegeModalRef(component: Component, captureCollege: CaptureCollege): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.captureCollege = captureCollege;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
