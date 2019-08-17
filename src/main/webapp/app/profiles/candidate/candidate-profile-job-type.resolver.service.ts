/*import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { ProfileHelperService } from '../profile-helper.service';
import { Observable } from 'rxjs/Observable';
import { JobType } from '../../entities/job-type/job-type.model'; 

@Injectable()
export class CandidateJobTypeResolverService implements Resolve<JobType[]> {
    constructor (private profileHelperService: ProfileHelperService,private router: Router){}

    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<JobType[]>{
        return this.profileHelperService.getJobType()
              .catch((error: any )=> {
          //          console.log (`${error}`);
                    this.router.navigate(['/error']);
                    return Observable.of(null);
                })
      
    }
}*/