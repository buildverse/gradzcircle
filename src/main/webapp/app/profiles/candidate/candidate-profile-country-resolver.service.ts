import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { ProfileHelperService } from '../profile-helper.service';
import { Observable } from 'rxjs/Observable';
import { Country } from '../../entities/country/country.model';

@Injectable()
export class CountryResolverService implements Resolve<Country[]> {
    constructor (private profileHelperService: ProfileHelperService,private router : Router){}
    
    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Country[]>{
        return this.profileHelperService.getCountries()
            .catch((error: any )=> {
            console.log (`${error}`);
            this.router.navigate(['/error']);
            return Observable.of(null);
        })
    }
}