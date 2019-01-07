

import {BaseEntity, User} from './../../shared';

export class CandidateList implements BaseEntity {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public qualificationWithHighestCourse?: string,
    public login?: User,
    public reviewed?: boolean,
    public jobId?: number
  ) {
    this.reviewed = false;
  }
}
