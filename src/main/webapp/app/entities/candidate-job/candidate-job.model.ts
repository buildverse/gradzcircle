import { Job } from '../job/job.model';
import { Candidate } from '../candidate/candidate.model';

export class CandidateJob {
    constructor(
        public candidateJobId?: number,
        public job?: Job,
        public canidate?: Candidate,
        public matchScore?: number,
        public educationMatchScore?: number,
        public genderMatchScore?: number,
        public languageMatchScore?: number,
        public totalEligibleScore?: number
    ) {}
}
