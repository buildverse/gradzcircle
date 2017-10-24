import { BaseEntity } from './../../shared';

export class College implements BaseEntity {
    constructor(
        public id?: number,
        public collegeName?: string,
        public domainName?: string,
        public status?: number,
        public candidateEducations?: BaseEntity[],
        public university?: BaseEntity,
    ) {
    }
}
