import { BaseEntity } from './../../shared';

export class Qualification implements BaseEntity {
    constructor(
        public id?: number,
        public qualification?: string,
        public candidateEducations?: BaseEntity[],
    ) {
    }
}
