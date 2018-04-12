import { BaseEntity } from './../../shared';

export class Qualification implements BaseEntity {
    constructor(
        public id?: number,
        public qualification?: string,
        public value?:string,
        public display?:string,
        public candidateEducations?: BaseEntity[],
    ) {
    }
}
