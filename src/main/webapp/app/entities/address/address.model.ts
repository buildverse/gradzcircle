import { BaseEntity } from './../../shared';

export class Address implements BaseEntity {
    constructor(
        public id?: number,
        public addressLineOne?: string,
        public addressLineTwo?: string,
        public city?: string,
        public state?: string,
        public zip?: string,
        public candidate?: BaseEntity,
        public country?: BaseEntity,
    ) {
    }
}
