import { BaseEntity } from './../../shared';

export class CaptureUniversity implements BaseEntity {
    constructor(
        public id?: number,
        public universityName?: string,
        public capturecollege?: BaseEntity,
    ) {
    }
}
