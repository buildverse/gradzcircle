import { CandidateEducation } from '../candidate-education/candidate-education.model';
export class CaptureCourse {
    constructor(public id?: number, public courseName?: string, public candidateEducation?: CandidateEducation) {}
}
