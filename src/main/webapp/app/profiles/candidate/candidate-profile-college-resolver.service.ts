/*import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { ProfileHelperService } from '../profile-helper.service';
import { Observable } from 'rxjs/Observable';
import { College } from '../../entities/college/college.model';

@Injectable()
export class CandidateAccountResolverService implements Resolve<College[]> {
    constructor (private profileHelperService: ProfileHelperService,private router: Router){}

    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<College[]>{
        return  this.profileHelperService.getColleges()
          .catch((error: any )=> {
          // console.log (`${error}`);
           this.router.navigate(['/error']);
           return Observable.of(null);
       })
      
      
    }
}*/