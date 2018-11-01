import { Injectable } from '@angular/core';
import { Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { CandidateLanguageProficiencyService } from './candidate-language-proficiency.service';
import { CandidateLanguageProficiency } from './candidate-language-proficiency.model'
import { HttpResponse } from '@angular/common/http';


@Injectable()
export class CandidateLanguageResolverService implements Resolve<HttpResponse<CandidateLanguageProficiency[]>> {
    private id: any;
    constructor(private candidateLanguageProficiencyService: CandidateLanguageProficiencyService, private router: Router) { }
  
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<HttpResponse<CandidateLanguageProficiency[]>> {
        this.id = route.params.id;
        return this.candidateLanguageProficiencyService.findByCandidateId(this.id).map(this.extractData)
            .catch((error: any) => {
                console.log(`${error}`);
                this.router.navigate(['/error']);
                return Observable.of(null);
            });
    }
  
  private extractData(response: HttpResponse<CandidateLanguageProficiency[]>) {
        let body = response.body;
        return body || {};
    }
}