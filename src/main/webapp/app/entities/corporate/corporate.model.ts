import { BaseEntity, User } from './../../shared';
import{ Country } from '../country';
import { Industry } from '../industry';

export class Corporate implements BaseEntity {
    constructor(
        public id?: number,
        public corporateName?: string,
        public corporateAddress?: string,
        public corporateCity?: string,
        public establishedSince?: any,
        public corporateEmail?: string,
        public corporateOverview?: string,
        public corporateBenefits?: string,
        public corporateWebsite?: string,
        public corporateFacebook?: string,
        public corporateTwitter?: string,
        public corporateInstagram?: string,
        public corporateLinkedIn?: string,
        public corporateCulture?: string,
        public contactPerson?: string,
        public corporatePhone?: string,
        public corporatePhoneCode?: string,
        public contactPersonDesignation?: string,
        public corporateTagLine?: string,
        public country?: Country,
        public industry?: Industry,
        public login?: User,
    ) {
    }
}
