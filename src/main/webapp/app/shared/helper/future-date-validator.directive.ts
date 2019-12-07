import { Directive } from '@angular/core';
import { NG_VALIDATORS } from '@angular/forms';
import { AbstractControl } from '@angular/forms';
import { Validator } from '@angular/forms';
import { JhiDateUtils } from 'ng-jhipster';
import { DATE_FORMAT } from '../../shared/constants/input.constants';

@Directive({
    selector: '[jhiNotFutureDate]',
    providers: [
        {
            provide: NG_VALIDATORS,
            useExisting: FutureDateValidatorDirective,
            multi: true
        }
    ]
})
export class FutureDateValidatorDirective implements Validator {
    constructor(private dateUtils: JhiDateUtils) {}

    validate(control: AbstractControl): { [key: string]: any } | null {
        const currentDate = new Date();
        let endDate;
        if (control.value) {
            endDate = new Date(control.value.format(DATE_FORMAT));
        }
        return endDate > currentDate ? { futureDate: true } : null;
    }
}
