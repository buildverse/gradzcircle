import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { CandidateLanguageProficiency } from '../../entities/candidate-language-proficiency/candidate-language-proficiency.model'
import { Principal } from '../../shared/auth/principal.service';
import { CandidateLanguageProficiencyService } from '../../entities/candidate-language-proficiency/candidate-language-proficiency.service';
import { Candidate } from '../../entities/candidate/candidate.model';
import { Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { ResponseWrapper } from '../../shared';

@Injectable()
export class CandidateLanguageProficiencyResolverService implements Resolve<CandidateLanguageProficiency> {
    candidateLanguageProficiencies: CandidateLanguageProficiency[];
    constructor (private candidateLanguageProficiencyService: CandidateLanguageProficiencyService,private router: Router){}
    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<CandidateLanguageProficiency>{
        let candidateId = route.parent.data['candidate'].id;
       // console.log("Candidate id is "+JSON.stringify(candidateId));
        return this.candidateLanguageProficiencyService.search({
                query: candidateId
                }).map((res: ResponseWrapper)=> this.candidateLanguageProficiencies).catch((error: any) => {
                        console.log(`${error}`);
                        this.router.navigate(['/error']);
                        return Observable.of(null);
                    }) ;
       
    }
    //   private extractData(response: ResponseWrapper) {
    //     let body = response.json;
    //     return body || {};
    // }
}