import { JobFilter } from '../job-filter/job-filter.model';
export class JobFilterHistory {
    constructor(public id?: number, public filterDescription?: string, public jobFilter?: JobFilter) {}
}
