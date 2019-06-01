
import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiLanguageService, JhiEventManager} from 'ng-jhipster';
import {Subscription} from 'rxjs/Rx';
import {ProfileService} from '../profiles/profile.service';
import {JhiLanguageHelper, Principal, LoginModalService, LoginService, UserService, DataStorageService} from '../../shared';
import {VERSION} from '../../app.constants';
import { CorporateService} from '../../entities/corporate';
import {Candidate, CandidateService} from '../../entities/candidate';
import { AuthoritiesConstants } from '../../shared/authorities.constant';
import { USER_ID, USER_TYPE, CORPORATE_ID, CANDIDATE_ID, USER_DATA, JOB_ID, MATCH_SCORE} from '../../shared/constants/storage.constants';
import { OnDestroy } from '@angular/core';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: [
    'navbar.css'
  ]
})
export class NavbarComponent implements OnInit, OnDestroy {

  inProduction: boolean;
  isNavbarCollapsed: boolean;
  prevNavbarState: boolean;
  languages: any[];
  swaggerEnabled: boolean;
  modalRef: NgbModalRef;
  eventSubscriber: Subscription;
  eventSubscriberUserImage: Subscription;
  version: string;
  userImage: string;
  defaultImage = require('../../../content/images/no-image.png');
  corporateId: number;
  candidateId: number;
  candidate: Candidate;
  noImage: boolean;
  authorities: string[];
  constructor(
    private loginService: LoginService,
    private languageService: JhiLanguageService,
    private languageHelper: JhiLanguageHelper,
    private principal: Principal,
    private loginModalService: LoginModalService,
    private profileService: ProfileService,
    private router: Router,
    private eventManager: JhiEventManager,
    private userService: UserService,
    private candidateService: CandidateService,
    private corporateService: CorporateService,
    private localStorageService: DataStorageService

  ) {
    this.version = VERSION ? 'v' + VERSION : '';
    this.isNavbarCollapsed = true;
  }

  ngOnInit() {
     this.isNavbarCollapsed = true;
    this.languageHelper.getAll().then((languages) => {
      this.languages = languages;
    });
    this.reloadUserImage();
     this.profileService.getProfileInfo().then((profileInfo) => {
            this.inProduction = profileInfo.inProduction;
            this.swaggerEnabled = profileInfo.swaggerEnabled;
        });
    this.registerChangeInImage();
    this.registerSuccessfulLogin();
    this.loadId();

  }

  ngOnDestroy() {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
    if (this.eventSubscriberUserImage) {
      this.eventManager.destroy(this.eventSubscriberUserImage); 

    }
  }
  
  registerSuccessfulLogin() {
    this.eventSubscriber = this.eventManager.subscribe('authenticationSuccess', (response) => {
      this.loadId();
      this.reloadUserImage();
    });
  }
  
  loadId() {
    if (!this.localStorageService.getData(USER_TYPE)) {
      this.principal.identity(true).then((user) => {
      //  console.log('Begin loading user info');
        if (user && user.authorities.indexOf('ROLE_CORPORATE') > -1) {
          this.corporateService.findCorporateByLoginId(user.id).subscribe((response) => {
            this.localStorageService.setdata(USER_TYPE, AuthoritiesConstants.CORPORATE);
            this.localStorageService.setdata(USER_ID, response.body.id);
            this.localStorageService.setdata(USER_DATA, JSON.stringify(response.body));
            this.corporateId = this.localStorageService.getData(USER_ID);
            this.eventManager.broadcast({
              name: 'userDataLoadedSuccess',
              content: 'User Data Load Success'
            });
           // console.log('Loaded Corporate info');
          });
        } else {
          if (user && user.authorities.indexOf('ROLE_CANDIDATE') > -1) {
            this.candidateService.getCandidateByLoginId(user.id).subscribe((response) => {
              this.localStorageService.setdata(USER_TYPE, AuthoritiesConstants.CANDIDATE);
              this.localStorageService.setdata(USER_ID, response.body.id);
              this.localStorageService.setdata(USER_DATA, JSON.stringify(response.body));
              this.candidateId = this.localStorageService.getData(USER_ID);
              this.eventManager.broadcast({
                name: 'userDataLoadedSuccess',
                content: 'User Data Load Success'
              });
            //  console.log('Loaded Candidate info');
            });
          } else if (user && user.authorities.indexOf('ROLE_ADMIN') > -1){
             this.localStorageService.setdata(USER_TYPE, AuthoritiesConstants.ADMIN);
            this.eventManager.broadcast({
                name: 'userDataLoadedSuccess',
                content: 'User Data Load Success'
              });
            //console.log('Loaded Admin info');
          }
        }
      });
    }
  }


  /*loadId() {
    this.principal.identity(true).then((user) => {
      if (user && user.authorities.indexOf('ROLE_CORPORATE') > -1) {
        this.corporateService.findCorporateByLoginId(user.id).subscribe((response) => {
          this.corporateId = response.body.id;
          this.localStorageService.setdata(USER_TYPE,AuthoritiesConstants.CORPORATE);
          this.localStorageService.setdata(USER_ID,this.corporateId);
          this.localStorageService.setdata(USER_DATA,response.body);
        });
      } else {
        if (user && user.authorities.indexOf('ROLE_CANDIDATE') > -1) {
          this.candidateService.getCandidateByLoginId(user.id).subscribe((response) => {
            this.candidate = response.body;
            this.candidateId = this.candidate.id;
            this.localStorageService.setdata(USER_TYPE,AuthoritiesConstants.CANDIDATE);
            this.localStorageService.setdata(USER_ID,this.candidateId);
            this.localStorageService.setdata(USER_DATA,response.body);
          });
        }
      }
    });
  }
  */
  changeLanguage(languageKey: string) {
    this.languageService.changeLanguage(languageKey);
  }

  collapseNavbar() {
    this.isNavbarCollapsed = true;
  }

  setCandidateRouterParamAndCollapse() {
    this.collapseNavbar();
    this.localStorageService.setdata(CANDIDATE_ID,this.candidateId);
  }
  
  setCorporateRouterParamAndCollapse() {
    this.collapseNavbar();
    this.localStorageService.setdata(CORPORATE_ID,this.corporateId);
  }
  
 
  
  isAuthenticated() {
    return this.principal.isAuthenticated();
  }

  login() {
    this.modalRef = this.loginModalService.open();
  }

  logout() {
    this.collapseNavbar();
    this.loginService.logout();
    this.candidateId = undefined;
    this.corporateId = undefined;
    this.localStorageService.removeData(USER_ID);
    this.localStorageService.removeData(USER_DATA);
    this.localStorageService.removeData(USER_TYPE);
    this.localStorageService.removeData(JOB_ID);
    this.localStorageService.removeData(CORPORATE_ID);
    this.localStorageService.removeData(CANDIDATE_ID);
    this.localStorageService.removeData(MATCH_SCORE);
    this.localStorageService.removeData('');
    this.router.navigate(['']);
  }

  toggleNavbar() {
      this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  getImageUrl() {
    return this.isAuthenticated() ? this.principal.getImageUrl() : null;
  }

  registerChangeInImage() {

    this.eventSubscriberUserImage = this.eventManager.subscribe('updateNavbarImage', ((response) => {
      setTimeout(() => {
        this.reloadUserImage();
      }, 0);
    })
    );
    this.eventSubscriber = this.eventManager.subscribe('userImageModification', (response) => this.reloadUserImage());
  }

  reloadUserImage() {
    this.noImage = false;
    this.principal.identity(true).then((user) => {

      if (user) {
        if (user.imageUrl !== undefined) {
          this.userService.getImageData(user.id).subscribe((response) => {
            if (response !== undefined) {
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
