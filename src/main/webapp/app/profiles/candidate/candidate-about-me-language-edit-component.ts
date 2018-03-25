import { Component, OnInit, EventEmitter, Input } from '@angular/core';
import { Principal } from '../../shared/auth/principal.service';
import { FormGroup, FormBuilder, Validators, AbstractControl, ValidatorFn, FormArray } from '@angular/forms';
import { JhiLanguageService } from 'ng-jhipster';
import 'rxjs/add/operator/debounceTime';
import { Observable } from 'rxjs/Observable';
import { NgbTypeahead, NgbDatepicker, NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { Candidate } from '../../entities/candidate/candidate.model';
import { CandidateService } from '../../entities/candidate/candidate.service';
import { CandidateLanguageProficiency } from '../../entities/candidate-language-proficiency/candidate-language-proficiency.model';
import { CandidateLanguageProficiencyService } from '../../entities/candidate-language-proficiency/candidate-language-proficiency.service';
import { Language } from '../../entities/language/language.model';
import {NgbModal, NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import { BaseEntity } from '../../shared/model/base-entity';

/**
 * @TODO: 
 *
 * 1. Update validations for type ahead need to ensure valid value is selected
 * 2. Clean up console logs
 * 
 * 
 */



@Component({
    moduleId: module.id,
    templateUrl: 'candidate-about-me-language-edit.component.html',
    styleUrls: ['candidate.css']
})

export class CandidateProfileLanguaugeEditComponent implements OnInit {
      @Input() name;
    candidateLanguageForm: FormGroup;
    candidateLanguageProficiencies: CandidateLanguageProficiency[];
    errorMessage: String;
    buttonText: String;
    languagesMetaData: Language[];
    candidate: Candidate[];
   

    get candidateLanguageProficiency(): FormArray{
        return <FormArray>this.candidateLanguageForm.get('candidateLanguageProficiency');
    }

    constructor(
        private principal: Principal,
        private formBuilder: FormBuilder,
        private languageService: JhiLanguageService,
        private route: ActivatedRoute,
        private router: Router,
        private candidateLanguageProficiencyService: CandidateLanguageProficiencyService
       
        
    ) { }


    ngOnInit() {
        
        this.route.data.subscribe((data: { language: Language[] }) => this.languagesMetaData = data.language);
        this.candidate = this.route.snapshot.parent.data['candidate'];
      //  console.log("Canddiate Details in langu component are : "+ JSON.stringify(this.candidate));
        this.candidateLanguageProficiencies = this.route.snapshot.parent.data['candidateLanguageProficiency'];
      //  console.log("Do i sget any thing from details "+ JSON.stringify(this.candidateLanguageProficiencies));
        this.candidateLanguageForm = this.formBuilder.group({      
            //candidateLanguageProficiency: this.formBuilder.array([this.buildLanguageGroup()]),
            language: [null,[Validators.required]],
            speakProficiency :[null],
            readProficiency :[null],
            writeProficiency :[null]
        });
        
        this.onCandidateLanguagesRetrieved();
        
    }


   
    // addCandidateLanguageProficiency(): void {
    //     this.candidateLanguageProficiency.push(this.buildLanguageGroup());
    // }

    //  removeCandidateLanguageProficiency(): void {
    //     this.candidateLanguageProficiency.removeAt(this.candidateLanguageProficiency.length -1);
    // }

  
     searchLanguages = (text$: Observable<string>) =>
        text$
            .debounceTime(200)
            .distinctUntilChanged()
            .map(term => term.length < 2 ? []
                : this.languagesMetaData.filter(v => v.language ?
                    v.language.toLowerCase().indexOf(term.toLowerCase()) > -1 : '').slice(0, 10));

    languageFormatter = (x: { language: string }) => x.language;

    onCandidateLanguagesRetrieved(): void {
      //  console.log('Candidate details are ' + JSON.stringify(this.candidateLanguageProficiencies?this.candidateLanguageProficiencies.length:''));
        if (this.candidateLanguageForm) {
            this.candidateLanguageForm.reset();
        }
        // if (this.candidateLanguageProficiencies?this.candidateLanguageProficiencies.length>0:0){
        //     this.setLanguageProficiency(this.candidateLanguageProficiencies);   
        // } 
    }

    // buildLanguageGroup():FormGroup {
    //     return this.formBuilder.group({
    //         language: [null,[Validators.required]],
    //         speakProficiency :[null],
    //         readProficiency :[null],
    //         writeProficiency :[null]
    //     })
    // }
    
    save(): void {
       // console.log("Calling save ()");
        if(this.candidateLanguageForm.dirty && this.candidateLanguageForm.valid){
            const formModel = this.candidateLanguageForm.value;
            const candidateProficiencyDeepCopy : CandidateLanguageProficiency[] = formModel.candidateLanguageProficiency.map(
                (candidateLanguageProficiency:CandidateLanguageProficiency) => Object.assign({},candidateLanguageProficiency)
            );
            for( var i = 0 ; i < candidateProficiencyDeepCopy.length ; i++)
                candidateProficiencyDeepCopy[i].candidate = this.candidate as BaseEntity;
          //   console.log("Mere bhai "+JSON.stringify(candidateProficiencyDeepCopy));
              this.candidateLanguageProficiencyService.createMultiple(candidateProficiencyDeepCopy as BaseEntity).subscribe(
                    ()=> this.onSaveComplete(),
                    (error: any)=> this.errorMessage=<any>error
              );
            // if(!this.candidateLanguageProficiencies || this.candidateLanguageProficiencies.length===0){
            //     let candidateLanguageProficiency = new CandidateLanguageProficiency();
            //     candidateLanguageProficiency.candidate = this.candidate;
            //     this.candidateLanguageProficiencies.push(candidateLanguageProficiency);
            //     let candidateLanguageData = Object.assign({},this.candidateLanguageProficiencies,this.candidateLanguageForm.value);

            //     this.candidateLanguageProficiencyService.create(candidateLanguageData).subscribe(
            //         ()=> this.onSaveComplete(),
            //         (error: any)=> this.errorMessage=<any>error
            //      );
            // }
            // else if (this.candidateLanguageProficiencies){
            //     let candidateLanguageData = Object.assign({},this.candidateLanguageProficiencies,this.candidateLanguageForm.value);

            //     this.candidateLanguageProficiencyService.update(candidateLanguageData).subscribe(
            //         ()=> this.onSaveComplete(),
            //         (error: any)=> this.errorMessage=<any>error
            //      );
            // }
            
           
        }
        else if(!this.candidateLanguageForm.dirty){
            this.onSaveComplete();
        }
       // this.router.navigate['candidate-profile/aboutMe'];
    }

    onSaveComplete():void {
        this.candidateLanguageForm.reset();
        this.router.navigate(['candidate-profile/profile'])
    }

    //  setLanguageProficiency (proficiencies : CandidateLanguageProficiency[]){
    //      const proficienciesFormGroup = proficiencies.map(proficiency => this.formBuilder.group(proficiency));
    //      const proficienciesFormArray = this.formBuilder.array(proficienciesFormGroup);
    //      this.candidateLanguageForm.setControl('candidateLanguageProficiency',proficienciesFormArray);
    //  }
   
}

