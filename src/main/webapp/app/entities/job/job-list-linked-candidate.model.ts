import {BaseEntity} from './../../shared';

export class JobListForLinkedCandidate implements BaseEntity {
  constructor(
    public id?: number,
    public jobTitle?: string,
    public matchScore?: number
  ) {

  }
}
