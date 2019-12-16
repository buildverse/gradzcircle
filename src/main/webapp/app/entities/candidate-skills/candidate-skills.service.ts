import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';
import { Skills } from '../skills/skills.model';
import { CandidateSkills } from './candidate-skills.model';
import { createRequestOption } from '../../shared';
import { map } from 'rxjs/operators';

export type EntityResponseType = HttpResponse<CandidateSkills>;

@Injectable()
export class CandidateSkillsService {
    private resourceUrl = SERVER_API_URL + 'api/candidate-skills';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/candidate-skills';
    private resourceGetSkillsForCandidateUrl = SERVER_API_URL + 'api/skills-for-candidate';

    constructor(private http: HttpClient) {}

    create(candidateSkills: CandidateSkills): Observable<EntityResponseType> {
        const copy = this.convert(candidateSkills);
        //  console.log("Pay Load ot server is "+JSON.stringify(copy));
        return this.http
            .post<CandidateSkills>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    update(candidateSkills: CandidateSkills): Observable<EntityResponseType> {
        const copy = this.convert(candidateSkills);
        return this.http
            .put<CandidateSkills>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<CandidateSkills>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
    }

    query(req?: any): Observable<HttpResponse<CandidateSkills[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CandidateSkills[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<CandidateSkills[]>) => this.convertArrayResponse(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<HttpResponse<CandidateSkills[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<CandidateSkills[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<CandidateSkills[]>) => this.convertArrayResponse(res)));
    }

    findSkillsByCandidateId(id: number): Observable<HttpResponse<CandidateSkills[]>> {
        return this.http
            .get<CandidateSkills[]>(`${this.resourceGetSkillsForCandidateUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: HttpResponse<CandidateSkills[]>) => this.convertArrayResponse(res)));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CandidateSkills = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    private convertArrayResponse(res: HttpResponse<CandidateSkills[]>): HttpResponse<CandidateSkills[]> {
        const jsonResponse: CandidateSkills[] = res.body;
        const body: CandidateSkills[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to CandidateSkills.
     */
    private convertItemFromServer(candidateSkills: CandidateSkills): CandidateSkills {
        const copy: CandidateSkills = Object.assign({}, candidateSkills);
        this.convertSkillsMetaForDisplay(copy);
        return copy;
    }

    /**
     * Convert a CandidateSkills to a JSON which can be sent to the server.
     */
    private convert(candidateSkills: CandidateSkills): CandidateSkills {
        const copy: CandidateSkills = Object.assign({}, candidateSkills);
        this.convertSkillsMetaForServer(copy);
        return copy;
    }

    private convertSkillsMetaForDisplay(candidateSkill: CandidateSkills) {
        const skillsArray = new Array();
        skillsArray.push(candidateSkill.skills);
        candidateSkill.skills = skillsArray;
    }

    private convertSkillsMetaForServer(candidateSkill: CandidateSkills) {
        // console.log('Data to serevr before converson is ' + JSON.stringify(candidateSkill));
        candidateSkill.skillsList = [];
        candidateSkill.skills.forEach(cskill => {
            const skill = new Skills();
            skill.skill = cskill.value;
            candidateSkill.skillsList.push(skill);
        });
        candidateSkill.skills = undefined;
        // console.log('Data to serevr after converson is ' + JSON.stringify(candidateSkill));
    }
}
