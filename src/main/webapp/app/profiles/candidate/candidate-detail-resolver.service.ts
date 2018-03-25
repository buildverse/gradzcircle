import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { Candidate } from '../../entities/candidate/candidate.model'
import { Principal } from '../../shared/auth/principal.service';
import { CandidateService } from '../../entities/candidate/candidate.service';
import { Observable } from 'rxjs/Observable';
import { BaseEntity } from '../../shared/model/base-entity';

@Injectable()
export class CandidateDetailResolverService implements Resolve<Candidate> {
    constructor (private candidateService: CandidateService,private router: Router){}

    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Candidate>{
        let candidateId = route.parent.data['candidate'].id;
        return this.candidateService.find(candidateId).catch((error: any) => {
            console.log(`${error}`);
            this.router.navigate(['/error']);
            return Observable.of(null);
        });
    }
}