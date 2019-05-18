import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { Skills } from '../../entities/skills/skills.model';
import { ProfileHelperService } from '../profile-helper.service';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class CandidateSkillsResolverService implements Resolve<Skills[]> {
    constructor (private profileHelperService: ProfileHelperService,private router: Router){}

    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Skills[]>{
        return  this.profileHelperService.getSkills()
              .catch((error: any) => {
                       // console.log(`${error}`);
                        this.router.navigate(['/error']);
                        return Observable.of(null);
                    }); 
      
      
    }
}