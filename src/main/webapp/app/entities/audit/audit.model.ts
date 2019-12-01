export class Audit {
    constructor(
        public id?: number,
        public createdBy?: number,
        public updatedBy?: number,
        public createdTime?: any,
        public updatedTime?: any
    ) {}
}
