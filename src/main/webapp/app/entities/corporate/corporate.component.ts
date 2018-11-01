import {Component, OnInit, OnDestroy, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService} from 'ng-jhipster';
import {AuthoritiesConstants} from '../../shared/authorities.constant';
import {Corporate} from './corporate.model';
import {CorporateService} from './corporate.service';
import {ITEMS_PER_PAGE, Principal, UserService} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';
import {LocalStorageService, SessionStorageService} from 'ngx-webstorage';
import {FileSelectDirective, FileUploader, FileLikeObject} from 'ng2-file-upload';
import {SERVER_API_URL} from '../../app.constants';

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
  corporateId: number;
  imageUrl: any;
  noImage: boolean;
  defaultImage = require('../../../content/images/no-image.png');
  fileUploadErrorMessage: string;
  allowedMimeType = ['image/png', 'image/jpg', 'image/jpeg', 'image/gif'];
  maxFileSize = 100 * 1024;
  imageDataNotAvailable: boolean;
  uploader: FileUploader;
  queueLimit = 1;

  constructor(
    private corporateService: CorporateService,
    private jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private activatedRoute: ActivatedRoute,
    private principal: Principal,
    private userService: UserService,
    private localStorage: LocalStorageService,
    private sessionStorage: SessionStorageService,
    private router: Router,
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
  //  ngOnInit() {
  //      this.loadAll();
  //      this.principal.identity().then((account) => {
  //          this.currentAccount = account;
  //      });
  //      this.registerChangeInCorporates();
  //  }

  ngOnInit() {
    this.noImage = true;
    // this.imageDataNotAvailable=true;
    this.eventManager.broadcast({name: 'updateNavbarImage', content: 'OK'});
    this.principal.identity().then((account) => {
      this.currentAccount = account;
      if (account.authorities.indexOf(AuthoritiesConstants.CORPORATE) > -1) {
        this.corporateService.findCorporateByLoginId(account.id).subscribe((response) => {
          this.corporate = response.body;
          this.currentCorporate = this.corporate.id.toString();
          const token = this.localStorage.retrieve('authenticationToken') || this.sessionStorage.retrieve('authenticationToken');
          this.uploader = new FileUploader({
            url: SERVER_API_URL + 'api/upload/' + this.corporate.login.id,
            allowedMimeType: this.allowedMimeType,
            maxFileSize: this.maxFileSize,
            queueLimit: this.queueLimit,
            removeAfterUpload: true
          });
          this.uploader.authTokenHeader = 'Authorization';
          this.uploader.authToken = 'Bearer ' + token;
          this.uploader.onWhenAddingFileFailed = (item, filter, options) => this.onWhenAddingFileFailed(item, filter, options);
          this.uploader.onAfterAddingFile = (item) => this.onAfterAddingFile(item);
          this.reloadCorporateImage();
        });
      } else {
        this.loadAll();
      }

    });
    this.registerChangeInCorporates();
    this.registerChangeInCorporateImage();
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

  uploadImage(item) {
    item.upload();
    this.uploader.onCompleteItem = (item, response, status, header) => {
      if (status === 200) {
        this.eventManager.broadcast({name: 'corporateImageModification', content: 'OK'});
        this.router.navigate(['/corporate']);
      }
    }


  }
  onAfterAddingFile(item) {
    this.fileUploadErrorMessage = '';
  }
  onWhenAddingFileFailed(item: FileLikeObject, filter: any, options: any) {

    switch (filter.name) {
      case 'fileSize':
        this.fileUploadErrorMessage = `File size must not be more than 100 Kb`;
        break;
      case 'mimeType':
        const allowedTypes = this.allowedMimeType.join();
        this.fileUploadErrorMessage = `File types allowed : png,jpg,jpeg,gif`;
        break;
      default:
        this.fileUploadErrorMessage = `Unknown error (filter is ${filter.name})`;
    }

  }

  clearSelectedPicture() {
    this.uploader.clearQueue();
    this.selectedPicture.nativeElement.value = '';
  }

  removeImage() {
    let status;
    this.userService.deleteImage(this.corporate.login.id).subscribe(response => {
      status = response.status;
      if (status === 200) {
        this.eventManager.broadcast({name: 'corporateImageModification', content: 'OK'});
        this.router.navigate(['/corporate']);
      }
    });


  }

  reloadCorporateImage() {
    this.principal.identity(true).then((user) => {
      if (user) {
        if (user.imageUrl) {
          this.userService.getImageData(user.id).subscribe(response => {
            let responseJson = response.body;
            this.imageUrl = responseJson[0].href + '?t=' + Math.random().toString();
          });
          this.noImage = false;
        }
        else
          this.noImage = true;
      }

    });
  }
}

