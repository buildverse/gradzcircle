import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable()
export class CandidateProfileScoreService {

  private messageSource = new Subject<number>();
  currentMessage = this.messageSource.asObservable();

  constructor(  ) { }

  changeScore(message: number) {
    this.messageSource.next(message);
  }

}