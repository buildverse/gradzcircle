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
        public candidate?: BaseEntity,
    ) {
        this.isCurrentActivity = false;
    }
}
