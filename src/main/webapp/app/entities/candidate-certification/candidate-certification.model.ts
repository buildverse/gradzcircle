import { BaseEntity } from './../../shared';

export class CandidateCertification implements BaseEntity {
    constructor(
        public id?: number,
        public certificationTitle?: string,
        public certificationDate?: any,
        public certificationDetails?: string,
        public candidate?: BaseEntity,
    ) {
    }
}
