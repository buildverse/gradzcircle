import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { ProfileHelperService } from '../profile-helper.service';
import { Observable } from 'rxjs/Observable';
import { Gender }  from '../../entities/gender/gender.model';

@Injectable()
export class CandidateGenderResolverService implements Resolve<Gender []> {
    constructor (private profileHelperService: ProfileHelperService,private router : Router){}

    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Gender []>{
          return this.profileHelperService.getGender()
          .catch((error: any )=> {
           console.log (`${error}`);
           this.router.navigate(['/error']);
           return Observable.of(null);
       })
      
      
    }
}