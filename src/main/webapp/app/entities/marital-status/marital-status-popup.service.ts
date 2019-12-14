import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { MaritalStatus } from './marital-status.model';
import { MaritalStatusService } from './marital-status.service';

@Injectable()
export class MaritalStatusPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(private modalService: NgbModal, private router: Router, private maritalStatusService: MaritalStatusService) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.maritalStatusService.find(id).subscribe((maritalStatusResponse: HttpResponse<MaritalStatus>) => {
                    const maritalStatus: MaritalStatus = maritalStatusResponse.body;
                    this.ngbModalRef = this.maritalStatusModalRef(component, maritalStatus);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.maritalStatusModalRef(component, new MaritalStatus());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    maritalStatusModalRef(component: Component, maritalStatus: MaritalStatus): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.maritalStatus = maritalStatus;
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
