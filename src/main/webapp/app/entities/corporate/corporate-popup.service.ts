import {Injectable, Component} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {Corporate} from './corporate.model';
import {CorporateService} from './corporate.service';
import {HttpResponse} from '@angular/common/http';

@Injectable()
export class CorporatePopupService {
  private ngbModalRef: NgbModalRef;

  constructor(
    private modalService: NgbModal,
    private router: Router,
    private corporateService: CorporateService

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
        this.corporateService.find(id)
          .subscribe((corporateResponse: HttpResponse<Corporate>) => {
            const corporate: Corporate = corporateResponse.body;
            if (corporate.establishedSince) {
              corporate.establishedSince = {
                year: corporate.establishedSince.getFullYear(),
                month: corporate.establishedSince.getMonth() + 1,
                day: corporate.establishedSince.getDate()
              };
            }
            this.ngbModalRef = this.corporateModalRef(component, corporate);
            resolve(this.ngbModalRef);
          });
      } else {
        // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
        setTimeout(() => {
          this.ngbModalRef = this.corporateModalRef(component, new Corporate());
          resolve(this.ngbModalRef);
        }, 0);
      }
    });
  }

  corporateModalRef(component: Component, corporate: Corporate): NgbModalRef {
    const modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.corporate = corporate;
    modalRef.result.then((result) => {
      this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true, queryParamsHandling: 'merge'});
      this.ngbModalRef = null;
    }, (reason) => {
      this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true, queryParamsHandling: 'merge'});
      this.ngbModalRef = null;
    });
    return modalRef;
  }
}
