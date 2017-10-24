import { BaseEntity } from './../../shared';

export class ErrorMessages implements BaseEntity {
    constructor(
        public id?: number,
        public componentName?: string,
        public errorKey?: string,
        public errorMessage?: string,
    ) {
    }
}
