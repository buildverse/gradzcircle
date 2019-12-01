import { Candidate } from '../candidate/candidate.model';

export class CandidateCertification {
    constructor(
        public id?: number,
        public certificationTitle?: string,
        public certificationDate?: any,
        public certificationDetails?: string,
        public candidate?: Candidate,
        public collapsed?: boolean
    ) {
        this.collapsed = true;
    }
}
