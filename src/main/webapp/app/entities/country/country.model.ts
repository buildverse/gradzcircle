import { Address } from '../address/address.model';
import { CandidateEmployment } from '../candidate-employment';
import { Corporate } from '../corporate/corporate.model';
import { Nationality } from '../nationality/nationality.model';
import { University } from '../university/university.model';
import { VisaType } from '../visa-type/visa-type.model';
export class Country {
    constructor(
        public id?: number,
        public countryName?: string,
        public shortCode?: string,
        public shortCodeThreeChar?: string,
        public countryNiceName?: string,
        public numCode?: number,
        public phoneCode?: number,
        public enabled?: boolean,
        public value?: string,
        public display?: string,
        public nationality?: Nationality,
        public addresses?: Address[],
        public universities?: University[],
        public candidateEmployments?: CandidateEmployment[],
        public visas?: VisaType[],
        public corporates?: Corporate[]
    ) {
        this.enabled = false;
    }
}
