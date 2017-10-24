import { Directive, forwardRef } from '@angular/core';
import { NG_VALIDATORS, FormControl } from '@angular/forms';

function validateNationalityFactory(nationalities) {
  return (c: FormControl) => {
    return nationalities.indexOf(c.value) ===-1 ? null : {
      validateNationality: {
        valid: false
      }
    };
  };
}

@Directive({
  selector: '[validateNationality][ngModel],[validateNationality][formControl]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: forwardRef(() => NationalityValidator), multi: true }
  ]
})
export class NationalityValidator {

  validator: Function;

  // constructor(NationalityBlackList: NationalityBlackList) {
  //   this.validator = validateNationalityFactory(NationalityBlackList);
  // }

  validate(c: FormControl) {
    return this.validator(c);
  }
}