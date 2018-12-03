import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Candidate} from '../../entities/candidate/candidate.model';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';
import {Subscription} from 'rxjs/Rx';
import {Principal, UserService} from '../../shared';

import {CandidateService} from '../../entities/candidate/candidate.service';
import {Observable} from 'rxjs/Observable';

import {HttpResponse, HttpErrorResponse} from '@angular/common/http';

@Component({
  moduleId: module.id,
  selector: 'jhi-candidate-profile',
  templateUrl: 'candidate-profile.component.html',
  styleUrls: ['candidate.css']

})

export class CandidateProfileComponent implements OnInit {

  candidate: Candidate;
  imageUrl: any;
  loginId: string;
  noImage: boolean;
  userImage: string;
  private eventSubscriber: Subscription;
  currentSearch: string;
  defaultImage = require('../../../content/images/no-image.png');
  detailsCollapsed: boolean;
  educationCollapsed: boolean;
  employmentCollapsed: boolean;
  nonAcademicCollapsed: boolean;
  certificationsCollapsed: boolean;
  languageCollapsed : boolean;
  activeTab: string;
  
  constructor(private route: ActivatedRoute,
    private eventManager: JhiEventManager,
    private alertService: JhiAlertService,
    private candidateService: CandidateService,

    private principal: Principal,
    private userService: UserService,
    private router: Router) {
    this.activeTab ='details';
    this.detailsCollapsed = true;
    this.educationCollapsed = true;
    this.employmentCollapsed = true;
    this.nonAcademicCollapsed = true;
    this.certificationsCollapsed = true;
    this.languageCollapsed = true;
    this.noImage = false;
  }

  toggleDetails() {
    this.detailsCollapsed = !this.detailsCollapsed;
    this.educationCollapsed = true;
    this.employmentCollapsed = true;
    this.nonAcademicCollapsed = true;
    this.languageCollapsed  = true;
    this.certificationsCollapsed = true;
  }

  toggleEducation() {
    this.detailsCollapsed = true;
    this.educationCollapsed = !this.educationCollapsed;
    this.employmentCollapsed = true;
    this.nonAcademicCollapsed = true;
    this.languageCollapsed  = true;
    this.certificationsCollapsed = true;
  }

  toggleEmployment() {
    this.detailsCollapsed = true;
    this.educationCollapsed = true;
    this.employmentCollapsed = !this.employmentCollapsed;
    this.nonAcademicCollapsed = true;
    this.languageCollapsed  = true;
    this.certificationsCollapsed = true;
  }
  toggleNonAcads() {
    this.detailsCollapsed = true;
    this.educationCollapsed = true;
    this.employmentCollapsed = true;
    this.nonAcademicCollapsed = !this.nonAcademicCollapsed;
    this.languageCollapsed  = true;
    this.certificationsCollapsed = true;
  }

  toggleCertification() {
    this.detailsCollapsed = true;
    this.educationCollapsed = true;
    this.employmentCollapsed = true;
    this.nonAcademicCollapsed = true;
    this.languageCollapsed  = true;
    this.certificationsCollapsed = !this.certificationsCollapsed;
  }
  
   toggleLanguage() {
    this.detailsCollapsed = true;
    this.educationCollapsed = true;
    this.employmentCollapsed = true;
    this.nonAcademicCollapsed = true;
    this.certificationsCollapsed = true;
     this.languageCollapsed = ! this.languageCollapsed;
  }
  
  setActiveTab( activeTab) {
    this.activeTab = activeTab;
  }
  
  ngOnInit() {
    this.route.data.subscribe((data: {candidate: any}) => this.candidate = data.candidate.body);
    this.currentSearch = this.candidate.id.toString();
    this.loginId = this.candidate.login.id;
    this.eventManager.broadcast({name: 'updateNavbarImage', content: 'OK'});
    this.reloadUserImage();
    // this.checkImageUrl();
    this.registerChangeInCandidateData();
    this.registerChangeInCandidateImage();
  }

  getCandidateByCandidateId(id) {
    this.candidateService.getCandidateByCandidateId(id).subscribe(
      (res: HttpResponse<Candidate>) => {
        this.candidate = res.body;
        this.loginId = this.candidate.login.id;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    return;
  }


  reloadCandidate() {
    this.candidateService.getCandidateByLoginId(this.loginId).subscribe(
      (res: HttpResponse<Candidate>) => this.candidate = res.body,
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    return;


  }

  private onError(error) {
    this.alertService.error(error.message, null, null);
  }
  registerChangeInCandidateData() {
    this.eventSubscriber = this.eventManager.subscribe('candidateListModification', (response) => this.reloadCandidate());
  }

  registerChangeInCandidateImage() {
    this.eventSubscriber = this.eventManager.subscribe('candidateImageModification', (response) => this.reloadUserImage());
  }

  reloadUserImage() {
    this.noImage = false;
    this.principal.identity(true).then((user) => {

      if (user) {
        if (user.imageUrl !== undefined) {
          this.userService.getImageData(user.id).subscribe((response) => {
            if (response !== undefined) {
              const responseJson = response.body;
              if (responseJson) {
                this.userImage = responseJson[0].href + '?t=' + Math.random().toString();
              } else {
                this.noImage = true;
              }
            }
          });
        } else {
          this.noImage = true;
        }
      }

    });
  }
}
