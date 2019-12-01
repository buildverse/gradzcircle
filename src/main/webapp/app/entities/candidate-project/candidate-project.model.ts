import { CandidateEducation } from '../candidate-education/candidate-education.model';
import { CandidateEmployment } from '../candidate-employment/candidate-employment.model';
export const enum ProjectType {
    'ACADEMIC',
    'SELF_INTEREST',
    'CORPORATE'
}

export class CandidateProject {
    constructor(
        public id?: number,
        public projectTitle?: string,
        public projectStartDate?: any,
        public projectEndDate?: any,
        public projectDescription?: string,
        public projectDuration?: number,
        public contributionInProject?: string,
        public isCurrentProject?: boolean,
        public educationId?: number,
        public employmentId?: number,
        public projectType?: ProjectType,
        public education?: CandidateEducation,
        public employment?: CandidateEmployment,
        public collapsed?: boolean
    ) {
        this.isCurrentProject = false;
        this.collapsed = true;
    }
}
