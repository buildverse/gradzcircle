import { Candidate } from '../candidate/candidate.model';
export class ProfileCategory {
    constructor(public id?: number, public categoryName?: string, public weightage?: number, public candidates?: Candidate[]) {}
}
