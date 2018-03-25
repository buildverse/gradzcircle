import { BaseEntity } from './../../shared';

export class JobFilterHistory implements BaseEntity {
    constructor(
        public id?: number,
        public filterDescription?: string,
        public jobFilter?: BaseEntity,
    ) {
    }
}
