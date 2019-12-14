import { Directive, Input } from '@angular/core';
import { NG_VALIDATORS, Validator, FormControl } from '@angular/forms';

@Directive({
    selector: '[jhi-customMin][formControlName], [jhi-customMin][formControl], [jhi-customMin] [ngModel]',
    providers: [{ provide: NG_VALIDATORS, useExisting: MinDirective, multi: true }]
})
export class MinDirective implements Validator {
    @Input() customMin: number;

    validate(c: FormControl): { [key: string]: any } {
        const v = c.value;
        return v < this.customMin ? { customMin: true } : null;
    }
}
