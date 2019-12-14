import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable()
export class JobListEmitterService {
    private messageSource = new Subject<boolean>();
    currentMessage = this.messageSource.asObservable();

    constructor() {}

    jobChanges(message: boolean) {
        this.messageSource.next(message);
    }
}
