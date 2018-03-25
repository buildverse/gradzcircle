import { Injectable } from '@angular/core';
import { Router,Resolve, RouterStateSnapshot,ActivatedRouteSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { Course } from '../course/course.model';
import { CourseService } from '../course/course.service';
import { Http, Response } from '@angular/http';

@Injectable()
export class CourseResolverService implements Resolve<Course []> {
    constructor (private courseService: CourseService,private router: Router){}

    resolve (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Course []>{
        return this.courseService.query().map(this.extractData)
        .catch((error: any )=> {
        console.log (`${error}`);
        this.router.navigate(['/error']);
        return Observable.of(null);
       });
    }

    private extractData(response: Response) {
        let body = response.json;
        return body || {};
    }
}