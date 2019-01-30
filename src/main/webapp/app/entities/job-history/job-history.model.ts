import { BaseEntity } from './../../shared';

export const enum PaymentType {
    'UPFRONT',
    'AS_YOU_GO'
}

export class JobHistory implements BaseEntity {
    constructor(
        public id?: number,
        public jobTitle?: string,
        public jobDescription?: string,
        public noOfApplicants?: number,
        public salary?: number,
        public jobStatus?: number,
        public createDate?: any,
        public originalJobCost?: number,
        public jobCost?: number,
        public amountPaid?: number,
        public totalAmountPaid?: number,
        public noOfApplicantsBought?: number,
        public removedFilterAmount?: number,
        public additionalFilterAmount?: number,
        public adminCharge?: number,
        public adminChargeRate?: number,
        public upfrontDiscountRate?: number,
        public upfrontDiscountAmount?: number,
        public escrowAmountUsed?: number,
        public escrowAmountAdded?: number,
        public paymentType?: PaymentType,
        public hasBeenEdited?: boolean,
        public everActive?: boolean,
        public canEdit?: boolean,
        public updateDate?: any,
        public createdBy?: number,
        public updatedBy?: number,
        public noOfApplicantLeft?: number,
        public jobType?: BaseEntity,
        public employmentType?: BaseEntity,
        public job?: BaseEntity,
    ) {
        this.hasBeenEdited = false;
        this.everActive = false;
        this.canEdit = false;
    }
}
