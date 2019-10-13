import { Injectable } from '@angular/core';
import { Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { HttpResponse } from '@angular/common/http';
import { DataStorageService } from '../../shared';
import { CANDIDATE_ID } from '../../shared/constants/storage.constants';
import { CandidateSkills } from './candidate-skills.model';
import { CandidateSkillsService } from './candidate-skills.service';

@Injectable()
export class CandidateCurrentSkillsResolverService implements Resolve<HttpResponse<CandidateSkills[]>> {
    private id: any;
    constructor(private candidateSkillsService: CandidateSkillsService, private router: Router,
    private dataService: DataStorageService) { }
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<HttpResponse<CandidateSkills[]>> {
      this.id = this.dataService.getData(CANDIDATE_ID);
        return this.candidateSkillsService.findSkillsByCandidateId(this.id).map(this.extractData)
            .catch((error: any) => {
                //console.log(`${error}`);
                this.router.navigate(['/error']);
                return Observable.of(null);
            });
    }
  
  private extractData(response: HttpResponse<CandidateSkills[]>) {
        let body = response.body;
        return body || {};
    }
}