import { Injectable } from '@angular/core';
import { Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { Principal } from '../../shared/auth/principal.service';
import {CorporateService } from './corporate.service';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class CorporateResolverService implements Resolve<any> {
    constructor(private principal: Principal, private router: Router, private corporateService: CorporateService) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
       // console.log("Am calling resolver for candidate");
        return  this.principal.identity().then((account) => {
             return this.corporateService.findCorporateByLoginId(account.id).toPromise()
                    .catch((error: any) => {
                     //   console.log(`${error}`);
                        this.router.navigate(['/error']);
                        return Observable.of(null);
                    });
        });
    }
}
