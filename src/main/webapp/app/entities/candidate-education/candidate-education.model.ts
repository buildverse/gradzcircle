import { BaseEntity } from './../../shared';
import {CandidateProject } from '../candidate-project/candidate-project.model';
 
export class CandidateEducation implements BaseEntity {
    constructor(
        public id?: number,
        public grade?: number,
        public educationFromDate?: any,
        public educationToDate?: any,
        public isPursuingEducation?: boolean,
        public gradeScale?: number,
        public highestQualification?: boolean,
        public roundOfGrade?: number,
        public gradeDecimal?: number,
        public capturedCourse?: string,
        public capturedQualification?: string,
        public capturedCollege?: string,
        public capturedUniversity?: string,
        public percentage?: number,
        public scoreType?: string,
        public educationDuration?: number,
        public candidate?: BaseEntity,
        public projects?: CandidateProject[],
        public qualification?: any,
        public course?: any,
        public college?: any,
        public collapsed? : boolean
    ) {
        this.isPursuingEducation = false;
        this.highestQualification = false;
      this.collapsed = true;
    }
}
