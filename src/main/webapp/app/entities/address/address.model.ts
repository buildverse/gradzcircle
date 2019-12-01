import { Country } from '../../entities/country';
import { Candidate } from '../../entities/candidate';
export class Address {
    constructor(
        public id?: number,
        public addressLineOne?: string,
        public addressLineTwo?: string,
        public city?: string,
        public state?: string,
        public zip?: string,
        public candidate?: Candidate,
        public country?: any
    ) {}
}
