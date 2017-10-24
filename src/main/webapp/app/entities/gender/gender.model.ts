import { BaseEntity } from './../../shared';

export class Gender implements BaseEntity {
    constructor(
        public id?: number,
        public gender?: string,
    ) {
    }
}
