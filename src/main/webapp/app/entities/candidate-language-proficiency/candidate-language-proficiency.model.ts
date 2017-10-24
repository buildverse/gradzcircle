import { BaseEntity } from './../../shared';
import { Language } from './../language/language.model';

export class CandidateLanguageProficiency implements BaseEntity {
    constructor(
        public id?: number,
        public proficiency?: string,
        public candidate?: BaseEntity,
        public language?: Language,
    ) {
    }
}
