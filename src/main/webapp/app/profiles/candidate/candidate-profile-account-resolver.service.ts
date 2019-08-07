import { Injectable } from '@angular/core';
import { Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { CandidateService } from '../../entities/candidate/candidate.service';
import { USER_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class CandidateResolverService implements Resolve<any> {
    constructor( private router: Router, private candidateService: CandidateService,  private dataService: DataStorageService) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
             return this.candidateService.getCandidateDetails(this.dataService.getData(USER_ID)).toPromise()
                    .catch((error: any) => {
                      //  console.log(`${error}`);
                        this.router.navigate(['/error']);
                        return Observable.of(null);
                    });
    }
}
