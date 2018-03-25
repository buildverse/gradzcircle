import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { JobCategory } from '../../entities/job-category/job-category.model'
import { Principal } from '../../shared/auth/principal.service';
import { ProfileHelperService } from '../profile-helper.service';
import { Observable } from 'rxjs/Rx';
@Injectable()
export class CandidateCareerInterestResolverService implements Resolve<JobCategory[]> {
    constructor (private profileHelper: ProfileHelperService,private router: Router){}

    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<JobCategory[]>{
        return this.profileHelper.getCareerInterests().catch((error: any) => {
                        console.log(`${error}`);
                        this.router.navigate(['/error']);
                        return Observable.of(null);
                    });       
    }
}