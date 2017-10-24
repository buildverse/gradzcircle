import { BaseEntity } from './../../shared';

export class JobCategory implements BaseEntity {
    constructor(
        public id?: number,
        public jobCategory?: string,
        public candidates?: BaseEntity[],
    ) {
    }
}
