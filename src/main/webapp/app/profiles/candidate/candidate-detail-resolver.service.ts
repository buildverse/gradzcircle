import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { Candidate } from '../../entities/candidate/candidate.model';
import { CandidateService } from '../../entities/candidate/candidate.service';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class CandidateDetailResolverService implements Resolve<Candidate> {
    constructor(private candidateService: CandidateService, private router: Router){}
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Candidate> {
        const candidateId = route.parent.data['candidate'].body.id;
        return this.candidateService.getCandidateByCandidateId(candidateId).catch((error: any) => {
           // console.log(`${error}`);
            this.router.navigate(['/error']);
            return Observable.of(null);
        });
    }
}