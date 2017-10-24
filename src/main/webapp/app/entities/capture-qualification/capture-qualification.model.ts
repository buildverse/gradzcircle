import { BaseEntity } from './../../shared';

export class CaptureQualification implements BaseEntity {
    constructor(
        public id?: number,
        public qualificationName?: string,
        public candidateEducation?: BaseEntity,
    ) {
    }
}
