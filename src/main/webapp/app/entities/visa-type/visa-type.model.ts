import { Candidate } from '../candidate/candidate.model';
import { Country } from '../country/country.model';
export class VisaType {
    constructor(public id?: number, public visa?: string, public candidates?: Candidate[], public country?: Country) {}
}
