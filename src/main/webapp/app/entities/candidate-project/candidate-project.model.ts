import { BaseEntity } from './../../shared';

export const enum ProjectType {
    'ACADEMIC',
    'SELF_INTEREST',
    'CORPORATE'
}

export class CandidateProject implements BaseEntity {
    constructor(
        public id?: number,
        public projectTitle?: string,
        public projectStartDate?: any,
        public projectEndDate?: any,
        public projectDescription?: string,
        public projectDuration?: number,
        public contributionInProject?: string,
        public isCurrentProject?: boolean,
        public projectType?: ProjectType,
        public education?: BaseEntity,
        public employment?: BaseEntity,
    ) {
        this.isCurrentProject = false;
    }
}
