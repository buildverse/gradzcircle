import { BaseEntity } from './../../shared';

export class States implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
    ) {
    }
}
