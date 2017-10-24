import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { ProfileHelperService } from '../profile-helper.service';
import { VisaType } from '../../entities/visa-type/visa-type.model';
import { Observable } from 'rxjs/Observable';


@Injectable()
export class CandidateVisaResolverService implements Resolve<VisaType[]> {
    constructor (private router : Router,
        private profileHelperService : ProfileHelperService){}

    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<VisaType[]>{
        return  this.profileHelperService.getVisa()
             .catch((error: any) => {
                        console.log(`${error}`);
                        this.router.navigate(['/error']);
                        return Observable.of(null);
                    }); 
      
    }
}