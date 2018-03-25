import { BaseEntity } from './../../shared';

export class EmploymentType implements BaseEntity {
    constructor(
        public id?: number,
        public employmentType?: string,
        public employmentTypeCost?: number,
        public candidateEmployments?: BaseEntity[],
    ) {
    }
}
