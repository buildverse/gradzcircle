import { User } from '../../core/user/user.model';
export class CandidateDetail {
    constructor(
        public id?: any,
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
        public imageUrl?: string
    ) {}
}
