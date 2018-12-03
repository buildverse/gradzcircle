import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';

import { JobCategory } from '../entities/job-category/job-category.model';
import { Gender } from '../entities/gender/gender.model';
import { MaritalStatus } from '../entities/marital-status/marital-status.model';
import { Country } from '../entities/country/country.model';
import { VisaType } from '../entities/visa-type/visa-type.model';
import { College } from '../entities/college/college.model';
import { University } from '../entities/university/university.model';
import { Qualification } from '../entities/qualification/qualification.model';
import { Language } from '../entities/language/language.model';
import { Skills } from '../entities/skills/skills.model';
import { EmploymentType } from '../entities/employment-type/employment-type.model';
import { JobType } from '../entities/job-type/job-type.model';
import { Nationality } from '../entities/nationality/nationality.model';

import 'rxjs/add/operator/do';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/map';
import 'rxjs/add/observable/of';

@Injectable()

export class ProfileHelperService {


  constructor(private http: HttpClient) { }

    getCareerInterests(): Observable<JobCategory[]> {
        return this.http.get('api/job-categories', {observe:'response'}).map((res: HttpResponse<any>) => this.extractData(res))
           // .do(data => console.log('getCareerInterests: ' + JSON.stringify(data)))
            .catch(this.handleError);
    }

    getGender(): Observable <Gender []>{
        return this.http.get('api/genders', {observe:'response'}).map((res: HttpResponse<any>) => this.extractData(res))
           // .do(data => console.log('getGender: '+JSON.stringify(data)))
            .catch(this.handleError);
    }

    getMaritalStatus (): Observable <MaritalStatus[]> {
        return this.http.get('api/marital-statuses',{observe:'response'}).map((res: HttpResponse<any>) => this.extractData(res))
           // .do(data=> console.log('getMaritalStatuses: '+JSON.stringify(data)))
            .catch(this.handleError);
    }

    getCountries(): Observable<Country[]> {
        return this.http.get('api/countries').map(this.extractData)
           // .do((data)=> console.log('getCountries: '+JSON.stringify(data)))
            .catch(this.handleError);
    }

    getNationalities(): Observable<Nationality[]>{
        return this.http.get('api/nationalities').map(this.extractData).catch(this.handleError);
    }
    getVisa (): Observable<VisaType[]> {
        return this.http.get('api/visa-types').map(this.extractData)
            //.do(data => console.log('visaTypes: '+JSON.stringify(data)))
            .catch(this.handleError);
    }

    getColleges (): Observable<College[]> {
        return this.http.get('api/colleges').map(this.extractData)
           // .do(data => console.log('colleges: '+JSON.stringify))
            .catch(this.handleError);
    }

    getQualification(): Observable <Qualification []> {
        return this.http.get('api/qualifications').map(this.extractData)
           // .do(data => console.log('qualifications: '+this.extractData))
            .catch(this.handleError);
    }

    getLanguages ():Observable <Language []> {
        return this.http.get('api/languages').map(this.extractData)
          //  .do(data => console.log('Languages: '+JSON.stringify(data)))
            .catch(this.handleError);
    }

    getSkills (): Observable < Skills []> {
        return this.http.get('api/skills').map(this.extractData)
           // .do(data => console.log('Skills: '+JSON.stringify(data)))
            .catch(this.handleError);
    }

    getEmploymentType (): Observable < EmploymentType []> {
        return this.http.get('api/employment-types').map(this.extractData)
           //.do(data => console.log('Employment - Types : '+this.extractData))
            .catch(this.handleError);
    }

    getJobType (): Observable < JobType []> {
        return this.http.get('api/job-types').map(this.extractData)
          //  .do(data => console.log('Job-Types : '+this.extractData))
            .catch(this.handleError);
    }
    
    handleError(error: HttpResponse<any>): Observable<any>{
        return Observable.throw(error.body || 'Server error');
    }

    private extractData(response: HttpResponse<any>) {
        let body = response.body;
        console.log("Body is :"+JSON.stringify(body));
        return body || {};
    }
}