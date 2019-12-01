import { CandidateDetail } from './candidate-detail.model';
import { Address } from '../address/address.model';
import { CandidateEducation } from '../candidate-education/candidate-education.model';
import { CandidateNonAcademicWork } from '../candidate-non-academic-work/candidate-non-academic-work.model';
import { CandidateCertification } from '../candidate-certification/candidate-certification.model';
import { CandidateEmployment } from '../candidate-employment/candidate-employment.model';
import { CandidateLanguageProficiency } from '../candidate-language-proficiency/candidate-language-proficiency.model';
import { CandidateSkills } from '../candidate-skills/candidate-skills.model';

export class CandidatePublicProfile {
    constructor(
        public id?: any,
        public isShortListed?: boolean,
        // public shortListedForCurrentJob?: boolean,
        public reviewed?: boolean,
        public candidateDetails?: CandidateDetail,
        public addresses?: Address[],
        public educations?: CandidateEducation[],
        public nonAcademics?: CandidateNonAcademicWork[],
        public certifications?: CandidateCertification[],
        public employments?: CandidateEmployment[],
        public candidateLanguageProficiencies?: CandidateLanguageProficiency[],
        public candidateSkills?: CandidateSkills[],
        public matchScore?: number,
        public canBeShortListed?: boolean
    ) {
        this.isShortListed = false;
        this.reviewed = false;
        this.canBeShortListed = false;
    }
}
