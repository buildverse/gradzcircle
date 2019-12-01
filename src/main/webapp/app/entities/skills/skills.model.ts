import { CandidateSkills } from '../candidate-skills/candidate-skills.model';
export class Skills {
    constructor(
        public id?: number,
        public skill?: string,
        public value?: string,
        public display?: string,
        public candidateSkills?: CandidateSkills[]
    ) {}
}
