import { BaseEntity } from './../../shared';

export class ProfileCategory implements BaseEntity {
    constructor(
        public id?: number,
        public categoryName?: string,
        public weightage?: number,
        public candidates?: BaseEntity[],
    ) {
    }
}
