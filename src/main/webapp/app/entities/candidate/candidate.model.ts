import { User } from '../../core/user/user.model';
import { Gender } from '../gender/gender.model';
import { MaritalStatus } from '../marital-status/marital-status.model';
import { Nationality } from '../nationality/nationality.model';
import { Address } from '../address/address.model';
import { CandidateEducation } from '../candidate-education/candidate-education.model';
import { CandidateNonAcademicWork } from '../candidate-non-academic-work/candidate-non-academic-work.model';
import { CandidateCertification } from '../candidate-certification/candidate-certification.model';
import { CandidateEmployment } from '../candidate-employment/candidate-employment.model';
import { CandidateLanguageProficiency } from '../candidate-language-proficiency/candidate-language-proficiency.model';
import { CandidateSkills } from '../candidate-skills';
import { JobCategory } from '../job-category/job-category.model';
import { VisaType } from '../visa-type/visa-type.model';
import { Job } from '../job/job.model';
import { Corporate } from '../corporate/corporate.model';
import { ProfileCategory } from '../profile-category/profile-category.model';

export class Candidate {
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
        public qualificationWithHighestCourse?: string,
        public profileScore?: number,
        // public job?: Job,
        // public corporate?: Corporate,
        public login?: User,
        public gender?: Gender,
        public maritalStatus?: MaritalStatus,
        public nationality?: any,
        public addresses?: Address[],
        public educations?: CandidateEducation[],
        public nonAcademics?: CandidateNonAcademicWork[],
        public certifications?: CandidateCertification[],
        public employments?: CandidateEmployment[],
        public candidateLanguageProficiencies?: CandidateLanguageProficiency[],
        public jobCategories?: JobCategory[],
        public candidateSkills?: CandidateSkills[],
        public visaType?: VisaType,
        public jobs?: Job[],
        public profileCategories?: ProfileCategory[],
        public hasEducationScore?: boolean,
        public hasEducation?: boolean,
        public hasCertification?: boolean,
        public hasEmployment?: boolean,
        public hasNonAcademic?: boolean,
        public hasLanguages?: boolean,
        public hasSkills?: boolean
    ) {
        this.differentlyAbled = false;
        this.availableForHiring = false;
        this.openToRelocate = false;
        this.matchEligible = false;
        // this.hasEducationScore = false;
        this.hasEducation = false;
        this.hasCertification = false;
        this.hasEmployment = false;
        this.hasNonAcademic = false;
        this.hasLanguages = false;
        this.hasSkills = false;
    }
}
