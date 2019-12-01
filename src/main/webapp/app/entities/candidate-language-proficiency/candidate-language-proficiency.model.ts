import { Candidate } from '../candidate/candidate.model';

export class CandidateLanguageProficiency {
    constructor(public id?: number, public proficiency?: string, public candidate?: Candidate, public language?: any) {}
}
