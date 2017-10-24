import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { ProfileHelperService } from '../profile-helper.service';
import { Observable } from 'rxjs/Observable';
import { Qualification } from '../../entities/qualification/qualification.model';

@Injectable()
export class CandidateQualificationResolverService implements Resolve<Qualification[]> {
    constructor (private profileHelperService: ProfileHelperService,private router : Router){}

    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Qualification[]>{
        return  this.profileHelperService.getQualification()
            .catch((error: any) => {
                        console.log(`${error}`);
                        this.router.navigate(['/error']);
                        return Observable.of(null);
                    }); 
      
    }
}