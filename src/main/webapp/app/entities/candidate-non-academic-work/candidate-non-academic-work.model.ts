import { Candidate } from '../candidate/candidate.model';
import { BaseEntity } from './../../shared';

export class CandidateNonAcademicWork implements BaseEntity {
    constructor(
        public id?: number,
        public nonAcademicInitiativeTitle?: string,
        public nonAcademicInitiativeDescription?: string,
        public duration?: number,
        public isCurrentActivity?: boolean,
        public roleInInitiative?: string,
        public nonAcademicWorkStartDate?: any,
        public nonAcademicWorkEndDate?: any,
        public candidate?: Candidate,
        public collapsed?: boolean
    ) {
        this.isCurrentActivity = false;
      this.collapsed = true;
    }
}
