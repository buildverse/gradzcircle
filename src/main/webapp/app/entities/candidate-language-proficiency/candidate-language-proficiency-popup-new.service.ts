import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CandidateLanguageProficiency } from './candidate-language-proficiency.model';
import { CandidateLanguageProficiencyService } from './candidate-language-proficiency.service';
import { Observable } from 'rxjs/Rx';
import { JhiAlertService } from 'ng-jhipster';
import { Candidate } from '../../entities/candidate/candidate.model';

@Injectable()
export class CandidateLanguageProficiencyPopupServiceNew {
    private ngbModalRef: NgbModalRef;
    private candidateLanguageProficiency: CandidateLanguageProficiency;
    // private currentCandidateLanguageProficiencies : CandidateLanguageProficiency[];
    private candidate: Candidate;
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private candidateLanguageProficiencyService: CandidateLanguageProficiencyService,
        private alertService: JhiAlertService

    ) { }

    // loadAlreadySavedProficienies (id:any){
    //     this.candidateLanguageProficiencyService.search( {query: id}).subscribe(
    //             (res: Response) => { this.currentCandidateLanguageProficiencies = res.json(); }, (res: Response) => this.onError(res.json())
    //             );
    //     console.log("Why am is not egttingthis fucker "+ this.currentCandidateLanguageProficiencies);
    // }

    open(component: Component, id?: number | any, currentCandidateLanguageProficiencies?: any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }
          //  console.log("ID in Lnguage proficeicny is " + JSON.stringify(id));
            if (id) {
                this.candidateLanguageProficiency = new CandidateLanguageProficiency();
                this.candidate = new Candidate();
                this.candidateLanguageProficiency.candidate = this.candidate;
                this.candidateLanguageProficiency.candidate.id = id;
                setTimeout(() => {
                    this.ngbModalRef = this.candidateLanguageProficiencyModalRef(component, this.candidateLanguageProficiency, currentCandidateLanguageProficiencies);
                    resolve(this.ngbModalRef);
                }, 0)


            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.candidateLanguageProficiencyModalRef(component, this.candidateLanguageProficiency, currentCandidateLanguageProficiencies);
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }
    // open(component: Component, id?: number | any, currentCandidateLanguageProficiencies?:any):Promise<NgbModalRef> {
    //     if (this.isOpen) {
    //         return;
    //     }
    //     console.log ( "Candidate id in pop new service is "+id);
    //    // this.loadAlreadySavedProficienies(id);
    //     this.isOpen = true;
    //     this.candidateLanguageProficiency = new CandidateLanguageProficiency();
    //     this.candidate = new Candidate();
    //     this.candidateLanguageProficiency.candidate = this.candidate;
    //     this.candidateLanguageProficiency.candidate.id = id;
    //     return this.candidateLanguageProficiencyModalRef(component, this.candidateLanguageProficiency,currentCandidateLanguageProficiencies);

    // }

    candidateLanguageProficiencyModalRef(component: Component, candidateLanguageProficiency: CandidateLanguageProficiency, currentCandidateLanguageProficiencies: CandidateLanguageProficiency[]): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.candidateLanguageProficiency = candidateLanguageProficiency;
        modalRef.componentInstance.currentCandidateLanguageProficiencies = currentCandidateLanguageProficiencies;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
