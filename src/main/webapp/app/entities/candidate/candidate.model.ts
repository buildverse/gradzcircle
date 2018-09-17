import { BaseEntity, User } from './../../shared';
import { Gender } from '../gender/gender.model';
import { MaritalStatus } from '../marital-status/marital-status.model';
import { Nationality } from '../nationality/nationality.model';
import { Address } from '../address/address.model'
import { CandidateEducation } from '../candidate-education/candidate-education.model';
import { CandidateNonAcademicWork } from '../candidate-non-academic-work/candidate-non-academic-work.model';
import { CandidateCertification } from '../candidate-certification/candidate-certification.model';
import { CandidateEmployment } from '../candidate-employment/candidate-employment.model';
import { CandidateLanguageProficiency } from '../candidate-language-proficiency/candidate-language-proficiency.model'
import { JobCategory } from '../job-category/job-category.model';
import { VisaType } from '../visa-type/visa-type.model';
import { Job } from '../job/job.model'

export class Candidate implements BaseEntity {
    constructor(
        public id?: number,
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
        public matchEligible?: boolean,
        public login?: User,
        public gender?: Gender,
        public maritalStatus?: MaritalStatus,
        public nationality?: Nationality,
        public addresses?: Address[],
        public educations?: CandidateEducation[],
        public nonAcademics?: CandidateNonAcademicWork[],
        public certifications?: CandidateCertification[],
        public employments?: CandidateEmployment[],
        public candidateLanguageProficiencies?: CandidateLanguageProficiency[],
        public jobCategories?: JobCategory[],
        public visaType?: VisaType,
        public jobs?: Job[],
    ) {
        this.differentlyAbled = false;
        this.availableForHiring = false;
        this.openToRelocate = false;
        this.matchEligible = false;
    }
}
