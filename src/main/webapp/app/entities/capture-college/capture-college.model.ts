import { BaseEntity } from './../../shared';

export class CaptureCollege implements BaseEntity {
    constructor(
        public id?: number,
        public collegeName?: string,
        public candidateEducation?: BaseEntity,
    ) {
    }
}
