import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { CountryService } from '../../entities/country/country.service';
import { Country } from '../../entities/country/country.model'
import { HttpResponse } from '@angular/common/http';

@Injectable()
export class CorporateRegisterResolver implements Resolve<Country[]> {
    constructor (private countryService: CountryService,private router: Router){}

    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<Country[]> {
        return this.countryService.getEnabledCountries().toPromise().then(
            (countries) => {
                return countries.body;
            }
        );
    }
}