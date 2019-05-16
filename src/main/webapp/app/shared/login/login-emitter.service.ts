import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable()
export class LoginEmitterService {

  private messageSource = new Subject<boolean>();
  currentMessage = this.messageSource.asObservable();

  constructor(  ) { }

  loginSuccess(message: boolean) {
    this.messageSource.next(message);
  }

}