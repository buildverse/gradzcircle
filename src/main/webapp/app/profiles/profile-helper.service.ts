import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';

import { JobCategory } from '../entities/job-category/job-category.model';
import { Gender } from '../entities/gender/gender.model';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/map';
import 'rxjs/add/observable/of';

@Injectable()
export class ProfileHelperService {
    constructor(private http: HttpClient) {}

    getCareerInterests(): Observable<JobCategory[]> {
        return (
            this.http
                .get('api/job-categories', { observe: 'response' })
                .map((res: HttpResponse<any>) => this.extractData(res))
                // .do((data) => console.log('getCareerInterests: ' + JSON.stringify(data)))
                .catch(this.handleError)
        );
    }

    getGender(): Observable<Gender[]> {
        return (
            this.http
                .get('api/genders', { observe: 'response' })
                .map((res: HttpResponse<any>) => this.extractData(res))
                // .do(data => console.log('getGender: '+JSON.stringify(data)))
                .catch(this.handleError)
        );
    }

    handleError(error: HttpResponse<any>): Observable<any> {
        return Observable.throw(error.body || 'Server error');
    }

    private extractData(response: HttpResponse<any>) {
        const body = response.body;
        return body || {};
    }
}
