import { Filter } from '../filter/filter.model';
export class FilterCategory {
    constructor(public id?: number, public filterCategory?: string, public filterCost?: number, public filters?: Filter[]) {}
}
