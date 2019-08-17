/*import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { ProfileHelperService } from '../profile-helper.service';
import { Observable } from 'rxjs/Observable';
import { Language } from '../../entities/language/language.model'

@Injectable()
export class CandidateLanguageResolverService implements Resolve<Language[]> {
    constructor (private profileHelperService: ProfileHelperService,private router: Router){}

    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Language[]>{
      return this.profileHelperService.getLanguages()
         .catch((error: any )=> {
      //     console.log (`${error}`);
           this.router.navigate(['/error']);
           return Observable.of(null);
       })
      
      
    }
}*/