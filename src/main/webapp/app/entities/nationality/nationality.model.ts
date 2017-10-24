import { BaseEntity } from './../../shared';

export class Nationality implements BaseEntity {
    constructor(
        public id?: number,
        public nationality?: string,
    ) {
    }
}
