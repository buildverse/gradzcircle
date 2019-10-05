import {Component, OnInit, OnDestroy, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';
import {AuthoritiesConstants} from '../../shared/authorities.constant';
import {Corporate} from './corporate.model';
import {CorporateService} from './corporate.service';
import {Principal, UserService} from '../../shared';
import {USER_ID, USER_DATA,USER_TYPE, CORPORATE_ID, LOGIN_ID} from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { Router } from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';

@Component({
  selector: 'jhi-corporate',
  templateUrl: './corporate.component.html',
  styleUrls: ['corporate.css']
})
export class CorporateComponent implements OnInit, OnDestroy {

  @ViewChild('selectedPicture')
  selectedPicture: any;
  corporates: Corporate[];
  corporate: Corporate;
  corporateId: number;
  currentAccount: any;
  eventSubscriber: Subscription;
  userLoadSubscriber: Subscription;
  currentSearch: string;
  currentCorporate: string;
  imageUrl: any;
  noImage: boolean;
  defaultImage = require('../../../content/images/placeholder.png');



  constructor(
    private corporateService: CorporateService,
    private jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private activatedRoute: ActivatedRoute,
    private principal: Principal,
    private userService: UserService,
     private localStorageService: DataStorageService,
     private spinnerService: NgxSpinnerService,
     private router: Router
  ) {
    this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
      this.activatedRoute.snapshot.params['search'] : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.corporateService.search({
        query: this.currentSearch,
      }).subscribe(
        (res: HttpResponse<Corporate[]>) => this.corporates = res.body,
        (res: HttpErrorResponse) => this.onError(res.message)

        );
      return;
    }
    this.corporateService.query().subscribe(
      (res: HttpResponse<Corporate[]>) => {
        this.corporates = res.body;
        this.currentSearch = '';
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  /*LOAD one corp  that is being edited.*/
  loadCorporate() {
    this.corporateService.find(this.corporate.id).subscribe(
      (res: HttpResponse<Corporate>) => this.corporate = res.body,
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.loadAll();
  }

  clear() {
    this.currentSearch = '';
    this.loadAll();
  }

  ngOnInit() {
    this.noImage = true;
    /*this.activatedRoute.data.subscribe((data: {corporate: any}) => this.corporate = data.corporate.body);
    this.currentSearch = this.corporate.id.toString();
    this.eventManager.broadcast({name: 'updateNavbarImage', content: 'OK'});
    this.reloadCorporateImage();
    this.registerChangeInCorporates();
    this.registerChangeInCorporateImage();*/
    if (this.localStorageService.getData(USER_TYPE) === AuthoritiesConstants.ADMIN) {
      this.loadAll();
    } else {
      this.corporate = JSON.parse(this.localStorageService.getData(USER_DATA));
      if (!this.corporate) {
        this.principal.identity(true).then((user) => {
          this.corporateService.findCorporateByLoginId(user.id).subscribe((response) => {
            this.localStorageService.setdata(USER_TYPE, AuthoritiesConstants.CORPORATE);
            this.localStorageService.setdata(USER_ID, response.body.id);
            this.localStorageService.setdata(USER_DATA, JSON.stringify(response.body));
            this.corporate = JSON.parse(this.localStorageService.getData(USER_DATA));
            this.corporateId = this.localStorageService.getData(USER_ID);
            this.currentSearch = this.corporate.id.toString();
            this.eventManager.broadcast({
              name: 'userDataLoadedSuccess',
              content: 'User Data Load Success'
            });
           // console.log('Loaded Corporate info');
          });
        });
      } else {
        this.currentSearch = this.corporate.id.toString();
      }
    }
    this.eventManager.broadcast({name: 'updateNavbarImage', content: 'OK'});
    this.reloadCorporateImage();
    this.registerChangeInCorporates();
    this.registerChangeInCorporateImage();
  }

  setRouterData() {
    this.localStorageService.setdata(CORPORATE_ID,this.corporate.id);
  }

  setProfilePicmgmtRouteParams() {
    this.localStorageService.setdata(LOGIN_ID, this.corporate.login.id);
  }

  ngOnDestroy() {
    if(this.userLoadSubscriber) {
      this.eventManager.destroy(this.userLoadSubscriber);
    }
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: Corporate) {
    return item.id;
  }

  registerChangeInCorporateImage() {
    this.eventSubscriber = this.eventManager.subscribe('userImageModification', (response) => this.reloadCorporateImage());
  }
  registerChangeInCorporates() {
    this.principal.identity().then((account) => {
      if (account.authorities.indexOf(AuthoritiesConstants.CORPORATE) > -1) {
        this.eventSubscriber = this.eventManager.subscribe('corporateListModification', (response) => this.loadCorporate());
      } else {
        this.eventSubscriber = this.eventManager.subscribe('corporateListModification', (response) => this.loadAll());
      }
    });

  }

  private onError(error) {
    this.jhiAlertService.error(error.message, null, null);
    this.router.navigate(['/error']);
  }

  reloadCorporateImage() {
    this.principal.identity(true).then((user) => {
      if (user) {
        if (user.imageUrl) {
          this.userService.getImageData(user.id).subscribe((response) => {
            const responseJson = response.body;
            this.imageUrl = responseJson[0].href + '?t=' + Math.random().toString();
          });
          this.noImage = false;
        } else {
          this.noImage = true;
        }
      }

    });
  }
}

