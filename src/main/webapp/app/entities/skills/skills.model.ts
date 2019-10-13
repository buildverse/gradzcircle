import {BaseEntity} from './../../shared';

export class Skills implements BaseEntity {
  constructor(
    public id?: number,
    public skill?: string,
    public value?: string,
    public display?: string,
    public candidateSkills?: BaseEntity[],
  ) {
  }
}
