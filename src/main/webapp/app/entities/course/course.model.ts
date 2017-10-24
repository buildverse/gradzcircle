import { BaseEntity } from './../../shared';

export class Course implements BaseEntity {
    constructor(
        public id?: number,
        public course?: string,
        public candidateEducations?: BaseEntity[],
    ) {
    }
}
