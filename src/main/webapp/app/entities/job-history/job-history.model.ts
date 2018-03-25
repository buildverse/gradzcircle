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
        public jobCost?: number,
        public amountPaid?: number,
        public escrowAmountUsed?: number,
        public additionalAmountCharge?: number,
        public paymentType?: PaymentType,
        public hasBeenEdited?: boolean,
        public everActive?: boolean,
        public canEdit?: boolean,
        public updateDate?: any,
        public createdBy?: number,
        public updatedBy?: number,
        public job?: BaseEntity,
    ) {
        this.hasBeenEdited = false;
        this.everActive = false;
        this.canEdit = false;
    }
}
