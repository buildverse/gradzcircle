import { BaseEntity } from './../../shared';

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
        public candidate?: BaseEntity,
        public projects?: BaseEntity[],
        public employmentType?: BaseEntity,
        public country?: BaseEntity,
        public jobType?: BaseEntity,
    ) {
        this.isCurrentEmployment = false;
    }
}
