import { CandidateEducation } from '../candidate-education/candidate-education.model';
export class Qualification {
    constructor(
        public id?: number,
        public qualification?: string,
        public value?: string,
        public display?: string,
        public weightage?: number,
        public category?: string,
        public candidateEducations?: CandidateEducation[]
    ) {}
}
