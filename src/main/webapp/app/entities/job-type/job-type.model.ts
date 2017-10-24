import { BaseEntity } from './../../shared';

export class JobType implements BaseEntity {
    constructor(
        public id?: number,
        public jobType?: string,
        public candidateEmployments?: BaseEntity[],
    ) {
    }
}
