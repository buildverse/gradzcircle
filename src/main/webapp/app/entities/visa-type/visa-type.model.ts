import { BaseEntity } from './../../shared';

export class VisaType implements BaseEntity {
    constructor(
        public id?: number,
        public visa?: string,
        public candidates?: BaseEntity[],
        public country?: BaseEntity,
    ) {
    }
}
