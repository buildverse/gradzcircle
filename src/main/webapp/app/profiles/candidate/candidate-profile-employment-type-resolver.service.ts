import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { ProfileHelperService } from '../profile-helper.service';
import { Observable } from 'rxjs/Observable';
import { EmploymentType } from '../../entities/employment-type/employment-type.model';

@Injectable()
export class CandidateEmploymentTypeResolverService implements Resolve<EmploymentType[]> {
    constructor (private profileHelperService: ProfileHelperService,private router: Router){}

    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<EmploymentType[]>{
        return  this.profileHelperService.getEmploymentType()
                 .catch((error: any )=> {
                    console.log (`${error}`);
                    this.router.navigate(['/error']);
                    return Observable.of(null);
       })
      
      
    }
}