

import {BaseEntity, User} from './../../shared';

export class CandidateList implements BaseEntity {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public qualificationWithHighestCourse?: string,
    public login?: User,
    public shortListed?: boolean,
    public jobId?: number,
    public corporateId?: number,
    public matchScore?: number,
    public canBuy?: boolean,
    public aboutMe?: string,
    public percent?: number
  ) {
    this.shortListed = false;
    this.canBuy = false;
  }
}
