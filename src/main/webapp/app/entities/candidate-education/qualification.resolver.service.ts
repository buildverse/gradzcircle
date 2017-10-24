import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { Qualification } from '../qualification/qualification.model';
import { QualificationService } from '../qualification/qualification.service';
import { Http, Response } from '@angular/http';
import {ResponseWrapper} from '../../shared';

@Injectable()
export class QualificationResolverService implements Resolve<Qualification []> {
    constructor (private qualificationService: QualificationService,private router : Router){}

    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Qualification []>{
        return this.qualificationService.query().map(this.extractData)
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