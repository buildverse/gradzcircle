import {Injectable, Component} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';

@Injectable()
export class ProfileMgmtPopupService {
  private ngbModalRef: NgbModalRef;
  constructor(
    private modalService: NgbModal,
    private router: Router,

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
        setTimeout(() => {
          this.ngbModalRef = this.candidateModalRef(component, id);
          resolve(this.ngbModalRef);
        }, 0);
      } else {
        // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
        setTimeout(() => {
          this.ngbModalRef = this.candidateModalRef(component);
          resolve(this.ngbModalRef);
        }, 0);
      }
    });
  }

  candidateModalRef(component: Component, candidateId?: number): NgbModalRef {
    const modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.candidateId = candidateId;
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
