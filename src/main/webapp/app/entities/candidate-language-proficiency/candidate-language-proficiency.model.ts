import { BaseEntity } from './../../shared';
import {Candidate } from '../candidate/candidate.model';
 


export class CandidateLanguageProficiency implements BaseEntity {
    constructor(
        public id?: number,
        public proficiency?: string,
        public candidate?: Candidate,
        public language?: any,
    ) {
    }
}
