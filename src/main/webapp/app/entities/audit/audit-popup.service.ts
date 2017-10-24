import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Audit } from './audit.model';
import { AuditService } from './audit.service';

@Injectable()
export class AuditPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private auditService: AuditService

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
                this.auditService.find(id).subscribe((audit) => {
                    audit.createdTime = this.datePipe
                        .transform(audit.createdTime, 'yyyy-MM-ddTHH:mm:ss');
                    audit.updatedTime = this.datePipe
                        .transform(audit.updatedTime, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.auditModalRef(component, audit);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.auditModalRef(component, new Audit());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    auditModalRef(component: Component, audit: Audit): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.audit = audit;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
