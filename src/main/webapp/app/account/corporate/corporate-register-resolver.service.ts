import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { CountryService } from '../../entities/country/country.service';
import { Country } from '../../entities/country/country.model'
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { ENABLED_COUNTRIES} from '../../shared/constants/storage.constants'

@Injectable()
export class CorporateRegisterResolver implements Resolve<Country[]> {
  constructor(private countryService: CountryService, private router: Router, private dataStorageService: DataStorageService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<Country[]> {
    if (this.dataStorageService.getData(ENABLED_COUNTRIES)) {
      return JSON.parse(this.dataStorageService.getData(ENABLED_COUNTRIES));
    } else {
      return this.countryService.getEnabledCountries().toPromise().then(
        (countries) => {
          this.dataStorageService.setdata(ENABLED_COUNTRIES,JSON.stringify(countries.body));
          return countries.body;
        }
      );
    }
  }
}