export class Filter {
    constructor(
        public id?: number,
        public filterName?: string,
        public filterCost?: number,
        public comments?: string,
        public matchWeight?: number
    ) {}
}
