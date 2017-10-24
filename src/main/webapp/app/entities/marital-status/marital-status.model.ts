import { BaseEntity } from './../../shared';

export class MaritalStatus implements BaseEntity {
    constructor(
        public id?: number,
        public status?: string,
    ) {
    }
}
