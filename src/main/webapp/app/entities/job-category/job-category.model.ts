import { Candidate } from '../candidate/candidate.model';
export class JobCategory {
    constructor(public id?: number, public jobCategory?: string, public candidates?: Candidate[]) {}
}
