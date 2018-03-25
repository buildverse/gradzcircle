import { BaseEntity } from './../../shared';

export class AppConfig implements BaseEntity {
    constructor(
        public id?: number,
        public configName?: string,
        public configValue?: boolean,
    ) {
        this.configValue = false;
    }
}
