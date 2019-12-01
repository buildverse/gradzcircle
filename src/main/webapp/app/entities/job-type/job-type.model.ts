import { CandidateEmployment } from '../candidate-employment/candidate-employment.model';
export class JobType {
    constructor(
        public id?: number,
        public jobType?: string,
        public jobTypeCost?: number,
        public candidateEmployments?: CandidateEmployment[]
    ) {}
}
