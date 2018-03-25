import { BaseEntity } from './../../shared';

export class JobFilter implements BaseEntity {
    constructor(
        public id?: number,
        public filterDescription?: any,
        public histories?: BaseEntity[],
        public job?: BaseEntity,
    ) {
    }
}
