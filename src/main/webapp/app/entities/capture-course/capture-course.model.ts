import { BaseEntity } from './../../shared';

export class CaptureCourse implements BaseEntity {
    constructor(
        public id?: number,
        public courseName?: string,
        public candidateEducation?: BaseEntity,
    ) {
    }
}
