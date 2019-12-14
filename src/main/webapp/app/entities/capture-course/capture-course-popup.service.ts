import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { CaptureCourse } from './capture-course.model';
import { CaptureCourseService } from './capture-course.service';

@Injectable()
export class CaptureCoursePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(private modalService: NgbModal, private router: Router, private captureCourseService: CaptureCourseService) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.captureCourseService.find(id).subscribe((captureCourseResponse: HttpResponse<CaptureCourse>) => {
                    const captureCourse: CaptureCourse = captureCourseResponse.body;
                    this.ngbModalRef = this.captureCourseModalRef(component, captureCourse);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.captureCourseModalRef(component, new CaptureCourse());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    captureCourseModalRef(component: Component, captureCourse: CaptureCourse): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.captureCourse = captureCourse;
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
