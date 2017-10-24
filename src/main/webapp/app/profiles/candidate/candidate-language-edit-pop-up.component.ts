import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { CandidateProfileLanguaugeEditComponent } from './candidate-about-me-language-edit-component';
import { Component } from '@angular/core';


@Component({
  selector: 'test',
  templateUrl: ''
})

export class CandidateLanguageEditPopUp {
  constructor(private modalService: NgbModal) {}

  open() {
    const modalRef = this.modalService.open(CandidateProfileLanguaugeEditComponent);
  }
}