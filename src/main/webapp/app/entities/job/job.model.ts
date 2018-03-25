import {Corporate} from '../corporate/corporate.model';
import {BaseEntity} from './../../shared';
import {JobType} from '../job-type/job-type.model';
import {EmploymentType} from '../employment-type/employment-type.model';
import {JobFilter} from '../job-filter/job-filter.model';

export const enum PaymentType {
  'UPFRONT',
  'AS_YOU_GO'
}

export class Job implements BaseEntity {
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
    public hasBeenEdited?: boolean,
    public everActive?: boolean,
    public updateDate?: any,
    public createdBy?: number,
    public updatedBy?: number,
    public jobFilters?: JobFilter[],
    public histories?: BaseEntity[],
    public jobType?: JobType,
    public canEdit?: boolean,
    public additionalAmountCharge?: number,
    public paymentType?: PaymentType,
    public employmentType?: EmploymentType,
    public corporate?: Corporate,
    public candidates?: BaseEntity[],
  ) {
    this.hasBeenEdited = false;
    this.everActive = false;
    this.canEdit = true;
  }
}
