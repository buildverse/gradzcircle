import { User } from '../../core/user/user.model';
import { Country } from '../country';
import { Job } from '../job';
import { Industry } from '../industry';

export class Corporate {
    constructor(
        public id?: number,
        public name?: string,
        public address?: string,
        public city?: string,
        public establishedSince?: any,
        public email?: string,
        public overview?: string,
        public benefits?: string,
        public website?: string,
        public facebook?: string,
        public twitter?: string,
        public instagram?: string,
        public linkedIn?: string,
        public culture?: string,
        public contactPerson?: string,
        public phone?: string,
        public phoneCode?: string,
        public personDesignation?: string,
        public tagLine?: string,
        public escrowAmount?: number,
        public country?: any,
        public industry?: Industry,
        public login?: User,
        public jobs?: Job[]
    ) {}
}
