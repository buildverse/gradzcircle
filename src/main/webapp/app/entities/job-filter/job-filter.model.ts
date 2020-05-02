import { JobFilterHistory } from '../job-filter-history/job-filter-history.model';
import { Job } from '../../shared/job-common/job.model';
export class JobFilter {
    constructor(public id?: number, public filterDescription?: any, public histories?: JobFilterHistory[], public job?: Job) {}
}
