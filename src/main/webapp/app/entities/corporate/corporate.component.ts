import {Component, OnInit, OnDestroy, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';
import {AuthoritiesConstants} from '../../shared/authorities.constant';
import {Corporate} from './corporate.model';
import {CorporateService} from './corporate.service';
import {Principal, UserService, DataService} from '../../shared';
import {USER_ID} from '../../shared/constants/storage.constants';

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
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;
  currentCorporate: string;
  imageUrl: any;
  noImage: boolean;
  defaultImage = require('../../../content/images/no-image.png');



  constructor(
    private corporateService: CorporateService,
    private jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private activatedRoute: ActivatedRoute,
    private principal: Principal,
    private userService: UserService,

    private dataService: DataService
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
    this.activatedRoute.data.subscribe((data: {corporate: any}) => this.corporate = data.corporate.body);
    this.currentSearch = this.corporate.id.toString();
    this.eventManager.broadcast({name: 'updateNavbarImage', content: 'OK'});
    this.reloadCorporateImage();
    this.registerChangeInCorporates();
    this.registerChangeInCorporateImage();
  }

  setRouterData() {
    this.dataService.setRouteData(this.corporate.id);
  }

  setProfilePicmgmtRouteParams() {
    this.dataService.put(USER_ID, this.corporate.login.id); 

  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: Corporate) {
    return item.id;
  }

  registerChangeInCorporateImage() {
    this.eventSubscriber = this.eventManager.subscribe('corporateImageModification', (response) => this.reloadCorporateImage());
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

