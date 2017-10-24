import {Component, Injectable} from '@angular/core';
import {Jsonp, URLSearchParams,ConnectionBackend} from '@angular/http';	
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/merge';

@Injectable()
export class EducationCollegeService {
  constructor(private _jsonp: Jsonp) {}

  search(term: string) {
    if (term === '') {
      return Observable.of([]);
    }

    let collegeServiceUrl = 'http://localhost:8080/api/_search/colleges';
    let params = new URLSearchParams();
    params.set('search', term);
    params.set('action', 'opensearch');
    params.set('format', 'json');
    params.set('callback', 'JSONP_CALLBACK');

    return this._jsonp
      .get(collegeServiceUrl, {search: params})
      .map(response => <string[]> response.json()[1]);
  }
}
