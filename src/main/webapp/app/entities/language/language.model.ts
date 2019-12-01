import { CandidateLanguageProficiency } from '../candidate-language-proficiency/candidate-language-proficiency.model';
export class Language {
    constructor(public id?: number, public language?: string, public candidateLanguageProficiencies?: CandidateLanguageProficiency[]) {}
}
