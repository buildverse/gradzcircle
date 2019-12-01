import { Candidate } from '../candidate/candidate.model';
import { Skills } from '../skills/skills.model';

export class CandidateSkills {
    constructor(
        public id?: number,
        public candidate?: Candidate,
        public skills?: any,
        public capturedSkills?: string,
        public display?: string,
        public value?: string,
        public skillName?: string,
        public skillsList?: Skills[]
    ) {}
}
