
import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiLanguageService, JhiEventManager} from 'ng-jhipster';
import {Subscription} from 'rxjs/Rx';
import {ProfileService} from '../profiles/profile.service';
import {JhiLanguageHelper, Principal, LoginModalService, LoginService, UserService} from '../../shared';
import {VERSION, DEBUG_INFO_ENABLED} from '../../app.constants';
import {SERVER_API_URL} from '../../app.constants';
import {Corporate, CorporateService} from '../../entities/corporate';
import {Candidate, CandidateService} from '../../entities/candidate';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: [
    'navbar.css'
  ]
})
export class NavbarComponent implements OnInit {

  inProduction: boolean;
  isNavbarCollapsed: boolean;
  languages: any[];
  swaggerEnabled: boolean;
  modalRef: NgbModalRef;
  eventSubscriber: Subscription;
  version: string;
  userImage: string;
  defaultImage = require('../../../content/images/no-image.png');
  corporateId: number;
  candidateId: number;
  candidate: Candidate;
  // imageUrl='http://localhost:8080/api/files/';
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
    private corporateService: CorporateService

  ) {
    this.version = VERSION ? 'v' + VERSION : '';
    this.isNavbarCollapsed = true;
  }

  ngOnInit() {
    //this.authorities = ['ROLE_CORPORATE'];
    //if (this.principal.hasAnyAuthorityDirect(this.authorities)) {
      this.principal.identity(true).then((user) => {
        if (user.authorities.indexOf('ROLE_CORPORATE')>-1) {
          this.corporateService.findCorporateByLoginId(user.id).subscribe((response) => {
            this.corporateId = response.id;
          });
        } else {
           if (user.authorities.indexOf('ROLE_CANDIDATE')>-1) {
          this.candidateService.getCandidateByLoginId(user.id).subscribe((response) => {
           // this.candidateId = response.id;
            this.candidate = response;
            this.candidateId = this.candidate.id;

            //console.log('Candidate in nav bar is '+JSON.stringify(this.candidate));
          });
        }
        }
      });

    //}
    this.languageHelper.getAll().then((languages) => {
      this.languages = languages;
    });
    this.reloadUserImage();
    this.profileService.getProfileInfo().subscribe((profileInfo) => {
      this.inProduction = profileInfo.inProduction;
      this.swaggerEnabled = profileInfo.swaggerEnabled;
    });
    this.registerChangeInImage();
  }

  changeLanguage(languageKey: string) {
    this.languageService.changeLanguage(languageKey);
  }

  collapseNavbar() {
    this.isNavbarCollapsed = true;
    //console.log('nav bar is '+ this.isNavbarCollapsed);
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
    this.router.navigate(['']);
  }

  toggleNavbar() {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
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
            if(response!==undefined) {
            //  console.log(JSON.stringify('------------'+response));
              const responseJson = response.json();
              this.userImage = responseJson[0].href + '?t=' + Math.random().toString();
            }
          });
        } else {
          this.noImage = true;
        }
      }

    });
  }
}
