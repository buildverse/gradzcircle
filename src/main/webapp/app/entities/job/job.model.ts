import {Corporate} from '../corporate/corporate.model';
import {EmploymentType} from '../employment-type/employment-type.model';
import {JobFilter} from '../job-filter/job-filter.model';
import {JobHistory} from '../job-history/job-history.model';
import {JobType} from '../job-type/job-type.model';
import {BaseEntity} from './../../shared';
import {CandidateJob} from '../candidate-job/candidate-job.model';
import {Candidate} from '../candidate';

export const enum PaymentType {
  UPFRONT = 'UPFRONT',
  AS_YOU_GO = 'AS_YOU_GO'
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
    public originalJobCost?: number,
    public jobCost?: number,
    public amountPaid?: number,
    public removedFilterAmount?: number,
    public additionalFilterAmount?: number,
    public adminCharge?: number,
    public totalAmountPaid?: number,
    public noOfApplicantsBought?: number,
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
    public noOfMatchedCandidates?: number,
    public noOfCandidatesApplied?: number,
    public matchScore?: number,
    public hasCandidateApplied?: boolean,
    public jobFilters?: JobFilter[],
    public histories?: JobHistory[],
    public jobType?: JobType,
    public employmentType?: EmploymentType,
    public corporate?: Corporate,
    public candidateJobs?: CandidateJob[],
    public appliedCandidates?: BaseEntity[],
    public totalNumberOfJobs?: number,
    public jobsLastMonth?: number,
    public newApplicants?: number,
    public totalLinkedCandidates?: number,
    public noOfShortListedCandidate?: number,
    public noOfApplicantLeft?: number,
    public corporateName?: string,
    public city?: string,
    public countOfPermanentEmployment?: number,
    public countOfContractEmployment?: number,
    public countOfFullTimeJob?: number,
    public countOfPartTimeJob?: number,
    public countOfInternJob?: number,
    public countOfSummerJob?: number,
    public numberOfCandidatesRemaining?: number,
    public payAsYouGo?: boolean,
    public filterCost?: number,
    public corporateEscrowAmount?: number,
    public corporateUrl?: string,
    public corporateLoginId?: any
  ) {
    this.hasBeenEdited = false;
    this.everActive = false;
    this.canEdit = false;
    this.hasCandidateApplied = false;
    this.payAsYouGo = false;
  }
}
