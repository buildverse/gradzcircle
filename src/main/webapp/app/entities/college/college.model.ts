import { CandidateEducation } from '../candidate-education/candidate-education.model';
import { University } from '../university/university.model';
export class College {
    constructor(
        public id?: number,
        public collegeName?: string,
        public domainName?: string,
        public value?: string,
        public display?: string,
        public affiliation?: string,
        public status?: number,
        public candidateEducations?: CandidateEducation[],
        public university?: University
    ) {}
}
