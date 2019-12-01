import { Corporate } from '../corporate/corporate.model';
export class Industry {
    constructor(public id?: number, public industryName?: string, public corporates?: Corporate[]) {}
}
