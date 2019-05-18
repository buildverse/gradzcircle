import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { ProfileHelperService } from '../profile-helper.service';
import { Observable } from 'rxjs/Observable';
import { MaritalStatus } from '../../entities/marital-status/marital-status.model';

@Injectable()
export class CandidateMaritalStatusResolverService implements Resolve<MaritalStatus[]> {
    constructor (private profileHelperService: ProfileHelperService,private router: Router){}

    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<MaritalStatus[]>{
       return this.profileHelperService.getMaritalStatus()
       .catch((error: any )=> {
          // console.log (`${error}`);
           this.router.navigate(['/error']);
           return Observable.of(null);
       });
      
    }
}