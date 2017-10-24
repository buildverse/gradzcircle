import { BaseEntity } from './../../shared';

export class Employability implements BaseEntity {
    constructor(
        public id?: number,
        public employableSkillName?: string,
        public employabilityScore?: number,
        public employabilityPercentile?: number,
    ) {
    }
}
