import { BaseEntity } from './../../shared';

export class Filter implements BaseEntity {
    constructor(
        public id?: number,
        public filterName?: string,
        public filterCost?: number,
        public comments?: string,
    ) {
    }
}
