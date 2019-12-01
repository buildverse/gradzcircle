import { CandidateEmployment } from '../candidate-employment/candidate-employment.model';
export class EmploymentType {
    constructor(
        public id?: number,
        public employmentType?: string,
        public employmentTypeCost?: number,
        public candidateEmployments?: CandidateEmployment[]
    ) {}
}
