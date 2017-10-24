import { BaseEntity } from './../../shared';

export class University implements BaseEntity {
    constructor(
        public id?: number,
        public universityName?: string,
        public country?: BaseEntity,
        public colleges?: BaseEntity[],
    ) {
    }
}
