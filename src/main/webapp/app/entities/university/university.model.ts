import { College } from '../college/college.model';
import { Country } from '../country/country.model';
export class University {
    constructor(
        public id?: number,
        public universityName?: string,
        public universityType?: string,
        public website?: string,
        public country?: Country,
        public colleges?: College[]
    ) {}
}
