import { BaseEntity } from './../../shared';
import { Country } from '../country/country.model';


export class Address implements BaseEntity {
    constructor(
        public id?: number,
        public addressLineOne?: string,
        public addressLineTwo?: string,
        public city?: string,
        public state?: string,
        public zip?: string,
        public country?: Country,

    ) {
    }
}
