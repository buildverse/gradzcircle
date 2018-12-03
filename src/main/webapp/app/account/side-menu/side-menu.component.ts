
import {Component, OnInit} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';
import {Subscription} from 'rxjs/Rx';
import {Router,ActivatedRoute} from '@angular/router';
import {Principal, UserService} from '../../shared';

import { CorporateService} from '../../entities/corporate';
import { CandidateService} from '../../entities/candidate';

@Component({
  templateUrl: './side-menu.component.html',
  styleUrls: [
    'side-menu.css'
  ]
})
export class SideMenuComponent implements OnInit {

  modalRef: NgbModalRef;
  eventSubscriber: Subscription;
  userImage: string;
  defaultImage = require('../../../content/images/no-image.png');
  corporateId: number;
  candidateId: number;
  candidate: any;
  corporate: any;
  user: any;
  noImage: boolean;
  authorities: string[];
  constructor(
    private principal: Principal,
    private eventManager: JhiEventManager,
    private userService: UserService,
     private route: ActivatedRoute,
    private candidateService: CandidateService,
    private corporateService: CorporateService

  ) {
  this.noImage = true;
  }

  ngOnInit() {
    this.candidate = this.route.snapshot.data['candidate'] ? this.route.snapshot.data['candidate'].body : null;
    this.corporate = this.route.snapshot.data['corporate'] ? this.route.snapshot.data['corporate'].body : null;
    if (this.candidate) {
      this.user = this.candidate.login;
    } else if (this.corporate) {
      this.user = this.corporate.login;
    }
    this.reloadUserImage();
    this.registerChangeInImage();
    //this.registerSuccessfulLogin();
  }

  registerSuccessfulLogin() {
      this.eventSubscriber = this.eventManager.subscribe('authenticationSuccess', (response) => this.loadId());
  }

  loadId() {
    console.log('Am I being called');
      this.principal.identity().then((user) => {
        if (user && user.authorities.indexOf('ROLE_CORPORATE') > -1) {
          this.user = user;
          this.corporateService.findCorporateByLoginId(user.id).subscribe((response) => {
            this.corporateId = response.body.id;
          });
        } else {
           if (user && user.authorities.indexOf('ROLE_CANDIDATE') > -1) {
             this.user = user;
          this.candidateService.getCandidateByLoginId(user.id).subscribe((response) => {
            this.candidateId = response.body.id;
          });
        }
        }
      });
  }


  isAuthenticated() {
    return this.principal.isAuthenticated();
  }

  getImageUrl() {
    return this.isAuthenticated() ? this.principal.getImageUrl() : null;
  }

  registerChangeInImage() {

    this.eventSubscriber = this.eventManager.subscribe('updateNavbarImage', ((response) => {
      setTimeout(() => {
        this.reloadUserImage();
      }, 0);
    })
    );
    this.eventSubscriber = this.eventManager.subscribe('candidateImageModification', (response) => this.reloadUserImage());
    this.eventSubscriber = this.eventManager.subscribe('corporateImageModification', (response) => this.reloadUserImage());
  }

  reloadUserImage() {
    this.noImage = false;
    this.principal.identity(true).then((user) => {

      if (user) {
        if (user.imageUrl !== undefined) {
          this.userService.getImageData(user.id).subscribe((response) => {
            if(response !== undefined) {
              const responseJson = response.body;
              if(responseJson) {
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
