import { BaseEntity } from './../../shared';

export class Country implements BaseEntity {
    constructor(
        public id?: number,
        public countryName?: string,
        public shortCode?: string,
        public shortCodeThreeChar?: string,
        public countryNiceName?: string,
        public numCode?: number,
        public phoneCode?: number,
        public enabled?: boolean,
        public nationality?: BaseEntity,
        public addresses?: BaseEntity[],
        public universities?: BaseEntity[],
        public candidateEmployments?: BaseEntity[],
        public visas?: BaseEntity[],
        public corporates?: BaseEntity[],
    ) {
        this.enabled = false;
    }
}
