import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { Nationality } from '../../entities/Nationality/nationality.model';
import { Principal } from '../../shared/auth/principal.service';
import { ProfileHelperService } from '../profile-helper.service';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class CandidateNationalityResolverService implements Resolve<Nationality[]> {
    constructor (private profileHelper: ProfileHelperService,private router: Router){}

    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Nationality[]>{
        return this.profileHelper.getNationalities().catch((error: any )=> {
          // console.log (`${error}`);
           this.router.navigate(['/error']);
           return Observable.of(null);
       });
    }
}