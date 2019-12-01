import { CandidateEducation } from '../candidate-education/candidate-education.model';
export class Course {
    constructor(public id?: number, public course?: string, public candidateEducations?: CandidateEducation[]) {}
}
