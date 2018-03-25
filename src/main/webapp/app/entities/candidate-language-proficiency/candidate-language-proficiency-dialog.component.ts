import { Component, OnInit, OnDestroy ,Input} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';
import { NgForm } from '@angular/Forms'
import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Subscription } from 'rxjs/Rx';
import { CandidateLanguageProficiency } from './candidate-language-proficiency.model';
import { CandidateLanguageProficiencyPopupService } from './candidate-language-proficiency-popup.service';
import { CandidateLanguageProficiencyPopupServiceNew } from './candidate-language-proficiency-popup-new.service';
import { CandidateLanguageProficiencyService } from './candidate-language-proficiency.service';
import { Candidate, CandidateService } from '../candidate';
import { Language, LanguageService } from '../language';
import { Principal } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-candidate-language-proficiency-dialog',
    templateUrl: './candidate-language-proficiency-dialog.component.html'
})
export class CandidateLanguageProficiencyDialogComponent implements OnInit {

    candidateLanguageProficiency: CandidateLanguageProficiency;
    isSaving: boolean;
    currentCandidateLanguageProficiencies: CandidateLanguageProficiency[];
    candidates: Candidate[];
    routerSub: any;
    languages: Language[];
    languageAlreadyPresentError: boolean;
    languagePresent: string;
    eventSubscriber: Subscription;
    editForm: NgForm;
    hasNoLanguageSelectedError: boolean;
    serverSideError:string;
    selectLanguageProficiency:boolean
    notSelectedLanguage:boolean;
    default:string;
    languageLocked: boolean;
   // action :string;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private candidateLanguageProficiencyService: CandidateLanguageProficiencyService,
        private candidateService: CandidateService,
        private languageService: LanguageService,
        private eventManager: JhiEventManager,
        private route: ActivatedRoute,
        private principal: Principal
    ) {
    }

    validateLanguage() {
        //console.log("langugae selected " + JSON.stringify(this.candidateLanguageProficiency));
        //console.log("Value passed is "+ JSON.stringify(value));
        this.serverSideError ='';
        if (!this.candidateLanguageProficiency.language) {
            this.hasNoLanguageSelectedError = true;
            this.languageAlreadyPresentError = false;
           // this.isLanguageAlreadyPresent();
        }
        else {
            this.notSelectedLanguage = false;
            this.hasNoLanguageSelectedError = false;
            this.isLanguageAlreadyPresent();
        }
    }
    resetErrors() {
        this.languageAlreadyPresentError = false;
    }

    isLanguageAlreadyPresent() {
        this.languageAlreadyPresentError = false;
        if (this.currentCandidateLanguageProficiencies) {

          //  console.log("Current languages is "+ JSON.stringify(this.currentCandidateLanguageProficiencies));
            for (var i = 0; i < this.currentCandidateLanguageProficiencies.length; i++) {

                if (this.candidateLanguageProficiency.language.id === this.currentCandidateLanguageProficiencies[i].language.id) {
                    this.languageAlreadyPresentError = true;
                    let language = this.candidateLanguageProficiency.language as Language;
                    this.languagePresent = language.language;
                }
            }

        }
    }
    ngOnInit() {
        this.isSaving = false;
        if(this.languageLocked){
            this.notSelectedLanguage = false;
            this.languageAlreadyPresentError = false;

        }
        else
            this.notSelectedLanguage=true;
        this.hasNoLanguageSelectedError = false;
        this.selectLanguageProficiency = false;
        if (this.principal.hasAnyAuthorityDirect(['ROLE_ADMIN'])) {
             this.candidateService.query()
            .subscribe((res: ResponseWrapper) => { this.candidates = res.json; }, (res: ResponseWrapper) => this.onError(res.json));

        }

        this.languageService.query()
            .subscribe((res: ResponseWrapper) => { this.languages = res.json; }, (res: ResponseWrapper) => this.onError(res.json));


    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

   checkSelectLanguageProficiency(){

       if(!this.candidateLanguageProficiency.language){
           this.notSelectedLanguage = true;
           return;
       }

        this.selectLanguageProficiency = false;
        if(this.candidateLanguageProficiency.language){
           if(this.candidateLanguageProficiency.proficiency)
                this.selectLanguageProficiency =true;

       }
   }

    save() {
        this.isSaving = true;
        if (this.candidateLanguageProficiency.id !== undefined) {
            this.subscribeToSaveResponse(
                this.candidateLanguageProficiencyService.update(this.candidateLanguageProficiency));
        } else {
            this.subscribeToSaveResponse(
                this.candidateLanguageProficiencyService.create(this.candidateLanguageProficiency));
        }

    }

    private subscribeToSaveResponse(result: Observable<CandidateLanguageProficiency>) {
        result.subscribe((res: CandidateLanguageProficiency) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: CandidateLanguageProficiency) {
        this.eventManager.broadcast({ name: 'candidateLanguageProficiencyListModification', content: 'OK'});
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

    trackLanguageById(index: number, item: Language) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-candidate-language-proficiency-popup',
    template: ''
})
export class CandidateLanguageProficiencyPopupComponent implements OnInit, OnDestroy {

    routeSub: any;
    languageLocked:boolean;
    //currentCandidateLanguageProficiencies: CandidateLanguageProficiency[];
   // candidateAllLanguageSelections : CandidateLanguageProficiency[];
    constructor(
        private route: ActivatedRoute,
        private candidateLanguageProficiencyPopupService: CandidateLanguageProficiencyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.languageLocked = true;
                this.candidateLanguageProficiencyPopupService
                    .open(CandidateLanguageProficiencyDialogComponent as Component, params['id'],this.languageLocked);
            } else {
                this.candidateLanguageProficiencyPopupService
                    .open(CandidateLanguageProficiencyDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }


}

@Component({
    selector: 'jhi-candidate-language-proficiency-popup',
    template: ''
})
export class CandidateLanguageProficiencyPopupComponentNew implements OnInit, OnDestroy {
    currentCandidateLanguageProficiencies: CandidateLanguageProficiency[];

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidateLanguageProficiencyPopupService: CandidateLanguageProficiencyPopupServiceNew
    ) { }

    ngOnInit() {
        this.route.data.subscribe((data: { currentLanguageSelections: CandidateLanguageProficiency[] }) => this.currentCandidateLanguageProficiencies = data.currentLanguageSelections);
        this.routeSub = this.route.params.subscribe((params) => {
            this.candidateLanguageProficiencyPopupService
                .open(CandidateLanguageProficiencyDialogComponent as Component, params['id'], this.currentCandidateLanguageProficiencies);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }


}
