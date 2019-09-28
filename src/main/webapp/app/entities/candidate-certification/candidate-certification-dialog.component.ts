import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { CandidateCertification } from './candidate-certification.model';
import { CandidateCertificationPopupService } from './candidate-certification-popup.service';
import { CandidateCertificationService } from './candidate-certification.service';
import { Candidate, CandidateService } from '../candidate';
import { CandidateCertificationPopupServiceNew } from './candidate-certification-popup-new.service';
import { DataStorageService } from '../../shared';
import { CANDIDATE_ID, CANDIDATE_CERTIFICATION_ID } from '../../shared/constants/storage.constants';

@Component({
    selector: 'jhi-candidate-certification-dialog',
    templateUrl: './candidate-certification-dialog.component.html'
})
export class CandidateCertificationDialogComponent implements OnInit {

    candidateCertification: CandidateCertification;
    isSaving: boolean;
    authorities: any[];
    candidates: Candidate[];
    certificationDateDp: any;
    editorConfig: any;
  
    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private candidateCertificationService: CandidateCertificationService,
        private candidateService: CandidateService,
        private eventManager: JhiEventManager,
        private config: NgbDatepickerConfig
    ) {
    }

    configureDatePicker() {
      this.config.minDate = {year: 1980, month: 1, day: 1};
      this.config.maxDate = {year: 2099, month: 12, day: 31};

      // days that don't belong to current month are not visible
      this.config.outsideDays = 'hidden';
    }
    ngOnInit() {
      this.configureDatePicker();
      this.editorConfig = {
        'toolbarGroups': [
          {'name': 'editing', 'groups': ['find', 'selection', 'spellchecker', 'editing']},
          {name: 'basicstyles', groups: ['basicstyles', 'cleanup']},
          {name: 'paragraph', groups: ['list', 'indent', 'align']},
        ],
        'removeButtons': 'Source,Save,Templates,Find,Replace,Scayt,SelectAll,forms'
        /* 'stylesSet': {name: 'para', element: 'p', styles: { 'margin': '0px' } }*/
      };
      this.isSaving = false;
      this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
      this.candidateService.query()
        .subscribe((res: HttpResponse<Candidate[]>) => {this.candidates = res.body;}, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.candidateCertification.id !== undefined) {
            this.subscribeToSaveResponse(
                this.candidateCertificationService.update(this.candidateCertification));
        } else {
            this.subscribeToSaveResponse(
                this.candidateCertificationService.create(this.candidateCertification));
        }
    }

     private subscribeToSaveResponse(result: Observable<HttpResponse<CandidateCertification>>) {
        result.subscribe((res: HttpResponse<CandidateCertification>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: CandidateCertification) {
        this.eventManager.broadcast({ name: 'candidateCertificationListModification', content: 'OK'});
       this.eventManager.broadcast({name: 'candidateListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCandidateById(index: number, item: Candidate) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-candidate-certification-popup',
    template: ''
})
export class CandidateCertificationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateCertificationPopupService: CandidateCertificationPopupService,
        private dataService: DataStorageService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.candidateCertificationPopupService
                    .open(CandidateCertificationDialogComponent as Component, params['id']);
            } else {
              const id = this.dataService.getData(CANDIDATE_CERTIFICATION_ID);
              if(id) {
                 this.candidateCertificationPopupService
                    .open(CandidateCertificationDialogComponent as Component,id);
              } else {
                this.candidateCertificationPopupService
                    .open(CandidateCertificationDialogComponent as Component);
              }
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

@Component({
    selector: 'jhi-candidate-certification-popup',
    template: ''
})
export class CandidateCertificationPopupComponentNew implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateCertificationPopupService: CandidateCertificationPopupServiceNew,
        private dataService: DataStorageService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
          if(params['id']) {
                this.candidateCertificationPopupService
                    .open(CandidateCertificationDialogComponent as Component, params['id']);
          } else {
            const id = this.dataService.getData(CANDIDATE_ID);
             this.candidateCertificationPopupService
                    .open(CandidateCertificationDialogComponent as Component, id);
          }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
