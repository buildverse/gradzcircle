import { Candidate } from '../../entities/candidate/candidate.model';
import {Injectable} from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import {Observable} from 'rxjs';


@Injectable()
export class CandidateProfileSettingService {

  private messageSource = new BehaviorSubject<any>('');
  
  private candidateFromParentToChild = new BehaviorSubject<Candidate>(undefined);
  
  private candidateFromChildToParent = new BehaviorSubject<Candidate>(undefined);

  getSetting(): Observable<any> {
    return this.messageSource.asObservable();
  }

  changeSetting(message: any) {
    this.messageSource.next(message);
  }
  
  setCandidateFromParentToChild(candidate: Candidate) {
    this.candidateFromParentToChild.next(candidate);
  }
  
  getCandidateFromParentToChild(): Observable<Candidate> {
    return this.candidateFromParentToChild.asObservable();
  }
  
  setCandidateFromChildToParent(candidate: Candidate) {
    this.candidateFromChildToParent.next(candidate);
  }
  
  getCandidateFromChildToParent(): Observable<Candidate> {
    return this.candidateFromChildToParent.asObservable();
  }

}