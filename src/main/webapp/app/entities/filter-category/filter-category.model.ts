import { BaseEntity } from './../../shared';

export class FilterCategory implements BaseEntity {
    constructor(
        public id?: number,
        public filterCategory?: string,
        public filterCost?: number,
        public filters?: BaseEntity[],
    ) {
    }
}
