import { CandidateEducation } from '../candidate-education/candidate-education.model';
export class CaptureQualification {
    constructor(public id?: number, public qualificationName?: string, public candidateEducation?: CandidateEducation) {}
}
