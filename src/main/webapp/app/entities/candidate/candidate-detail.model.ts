
import { BaseEntity, User } from './../../shared';

export class CandidateDetail implements BaseEntity {
    constructor(
        public id?: BaseEntity,
        public firstName?: string,
        public lastName?: string,
        public middleName?: string,
        public facebook?: string,
        public linkedIn?: string,
        public twitter?: string,
        public aboutMe?: string,
        public dateOfBirth?: any,
        public phoneCode?: string,
        public phoneNumber?: string,
        public differentlyAbled?: boolean,
        public availableForHiring?: boolean,
        public openToRelocate?: boolean,
        public qualificationWithHighestCourse?: string,
        public login?: User,
        ) {}
}
