import { BaseEntity } from './../../shared';
import { JobType } from '../job-type/job-type.model';
import { EmploymentType } from '../employment-type/employment-type.model';
import { CandidateProject } from '../candidate-project/candidate-project.model';
import {Candidate } from '../candidate/candidate.model';

export class CandidateEmployment implements BaseEntity {
    constructor(
        public id?: number,
        public location?: number,
        public jobTitle?: string,
        public employerName?: string,
        public employmentStartDate?: any,
        public employmentEndDate?: any,
        public employmentDuration?: number,
        public isCurrentEmployment?: boolean,
        public jobDescription?: string,
        public candidate?: Candidate,
        public projects?: CandidateProject[],
        public employmentType?: EmploymentType,
        public country?: BaseEntity,
        public jobType?: JobType,
        public collapsed?: boolean
    ) {
        this.isCurrentEmployment = false;
      this.collapsed = true;
    }
}
