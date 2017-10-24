import { BaseEntity } from './../../shared';

export class Audit implements BaseEntity {
    constructor(
        public id?: number,
        public createdBy?: number,
        public updatedBy?: number,
        public createdTime?: any,
        public updatedTime?: any,
    ) {
    }
}
