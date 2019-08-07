import {Directive} from '@angular/core';
import { NG_VALIDATORS } from '@angular/forms';
import {AbstractControl} from '@angular/forms';
import {Validator} from '@angular/forms';
import {JhiDateUtils} from 'ng-jhipster';


@Directive({
  selector: '[jhiNotFutureDate]',
  providers: [{
    provide: NG_VALIDATORS,
    useExisting: FutureDateValidatorDirective,
    multi: true
  }]
})
export class FutureDateValidatorDirective implements Validator {

  constructor(private dateUtils: JhiDateUtils) {}


  validate(control: AbstractControl): {[key: string]: any} | null {
    const currentDate = new Date();
    const endDate = new Date(this.dateUtils.convertLocalDateToServer(control.value));
    return endDate > currentDate ? {'futureDate': true} : null;
  }

}