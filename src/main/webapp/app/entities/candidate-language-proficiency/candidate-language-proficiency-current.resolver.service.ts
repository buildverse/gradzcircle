import { Injectable } from '@angular/core';
import { Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { CandidateLanguageProficiencyService } from './candidate-language-proficiency.service';
import { CandidateLanguageProficiency } from './candidate-language-proficiency.model'
import { Http, Response } from '@angular/http';
import { ResponseWrapper } from '../../shared';


@Injectable()
export class CandidateLanguageResolverService implements Resolve<CandidateLanguageProficiency[]> {
    private sub: any;
    private id: any;
    private res: any
    constructor(private candidateLanguageProficiencyService: CandidateLanguageProficiencyService, private router: Router) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<CandidateLanguageProficiency[]> {
        //console.log("My parama is "+JSON.stringify(route.firstChild));
        this.id = route.params.id;
        return this.candidateLanguageProficiencyService.findByCandidateId(this.id).map(this.extractData)
        //return this.candidateLanguageProficiencyService.search({ query: this.id }).map(this.extractData)
            .catch((error: any) => {
                console.log(`${error}`);
                this.router.navigate(['/error']);
                return Observable.of(null);
            });


    }

    private extractData(response: ResponseWrapper) {
        let body = response.json;
        return body || {};
    }
}