import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { College } from '../college/college.model';
import { CollegeService } from '../college/college.service';
import { Http, Response } from '@angular/http';
import { ResponseWrapper } from '../../shared';
@Injectable()
export class CollegeResolverService implements Resolve<College []> {
    constructor (private collegeService: CollegeService,private router: Router){}

    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<College []>{
        return this.collegeService.query().map(this.extractData)
        .catch((error: any )=> {
        console.log (`${error}`);
        this.router.navigate(['/error']);
        return Observable.of(null);
       });
    }

    private extractData(response: ResponseWrapper) {
        let body = response.json;
        return body || {};
    }
}