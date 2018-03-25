import { Injectable } from '@angular/core';
import { Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { Account } from '../../shared';
import { Principal } from '../../shared/auth/principal.service';
import { CandidateService } from '../../entities/candidate/candidate.service';
import { Candidate } from '../../entities/candidate/candidate.model';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class CandidateResolverService implements Resolve<any> {
    constructor(private principal: Principal, private router: Router, private candidateService: CandidateService) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
       // console.log("Am calling resolver for candidate");
        return  this.principal.identity().then((account) => {
             return this.candidateService.getCandidateByLoginId(account.id).toPromise()
                    .catch((error: any) => {
                        console.log(`${error}`);
                        this.router.navigate(['/error']);
                        return Observable.of(null);
                    });
        });
    }
}
