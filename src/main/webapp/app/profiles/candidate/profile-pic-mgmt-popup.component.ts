import {Component, OnInit, OnDestroy, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {DataStorageService} from '../../shared';
import {ProfileMgmtPopupService} from './profile-pic-mgmt-popup.service';
import {CANDIDATE_ID, USER_ID, USER_TYPE} from '../../shared/constants/storage.constants';
import {ImageCroppedEvent} from 'ngx-image-cropper';
import {LocalStorageService, SessionStorageService} from 'ngx-webstorage';
import {FileUploader, FileLikeObject} from 'ng2-file-upload';
import {SERVER_API_URL} from '../../app.constants';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';
import {Principal} from '../../shared/auth/principal.service';
import {UserService} from '../../shared/';
import { AuthoritiesConstants } from '../../shared/authorities.constant';

@Component({
  selector: 'jhi-candidate-profile-pic-dialog',
  templateUrl: './profile-pic.html'
 
})
export class ProfilePicMgmtPopupDialogComponent implements OnInit {

  @ViewChild('selectedPicture')
  selectedPicture: any;
  queueLimit = 1;
  showCropper: boolean = null;
  imageChangedEvent: any = '';
  croppedImage: any = '';
  uploader: FileUploader;
  fileUploadErrorMessage: string;
  allowedMimeType = ['image/png', 'image/jpg', 'image/jpeg'];
  candidateId: number;
  imageDataAvailable: boolean;
  corporateUser: boolean;

  constructor(
    public activeModal: NgbActiveModal,
    private localStorage: LocalStorageService,
    private sessionStorage: SessionStorageService,
    private eventManager: JhiEventManager,
    private principal: Principal,
    private userService: UserService,
    private dataService: DataStorageService
  ) {
    this.corporateUser = false;
  }

  ngOnInit() {
    this.imageDataAvailable = false;
    if (this.principal.getImageUrl()) {
      this.imageDataAvailable = true;
    }
    if(this.dataService.getData(USER_TYPE) === AuthoritiesConstants.CORPORATE) {
      this.corporateUser = true;
    }
    const token = this.localStorage.retrieve('authenticationToken') || this.sessionStorage.retrieve('authenticationToken');
    this.uploader = new FileUploader({
      url: SERVER_API_URL + 'api/upload/' + this.candidateId,
      allowedMimeType: this.allowedMimeType,
      queueLimit: this.queueLimit,
      removeAfterUpload: true
    });
    this.uploader.authTokenHeader = 'Authorization';
    this.uploader.authToken = 'Bearer ' + token;
    this.uploader.onWhenAddingFileFailed = (item, filter, options) => this.onWhenAddingFileFailed(item, filter, options);
    this.uploader.onAfterAddingFile = (item) => this.onAfterAddingFile(item);
  }

  clearSelectedPicture() {
    this.uploader.clearQueue();
    //  this.selectedPicture.nativeElement.value = '';
    //  this.showCropper = false;
  }

  fileChangeEvent(event: any): void {
    this.showCropper = true;
    this.imageChangedEvent = event;
  }

  removeImage() {
    let status;
    this.userService.deleteImage(this.candidateId).subscribe((response) => {
      status = response.status;
      if (status === 200) {
        this.eventManager.broadcast({name: 'userImageModification', content: 'OK'});
        }
        this.clear();
        // this.router.navigate(['../details'], {relativeTo: this.route});
    });
  }


  uploadImage(item) {
    item.upload();
    this.uploader.onCompleteItem = (item, response, status, header) => {
      if (status === 200) {
       this.eventManager.broadcast({name: 'userImageModification', content: 'OK'});
        this.clear();
      } else {
       // console.log('Status is ' + status + '   response is ' + JSON.stringify(response));
      }
    };
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
        this.fileUploadErrorMessage = `File types allowed : png, jpg, jpeg.`;
        break;
      case 'queueLimit':
        break;
      default:
        this.fileUploadErrorMessage = `Unknown error (filter is ${filter.name})`;
    }

  }

  imageCropped(event: ImageCroppedEvent) {
    this.croppedImage = event.file;
    this.uploader.queue[0]._file = this.croppedImage;
  }
  imageLoaded() {
    // show cropper
  }
  cropperReady() {
    // cropper ready
  }
  loadImageFailed() {
    // show message
  }

  clear() {
    this.clearSelectedPicture(); 
    this.activeModal.dismiss('cancel');
  }
}

@Component({
  selector: 'jhi-candidate-profile-pic-popup',
  template: ''
})
export class ProfilePicMgmtPopupComponent implements OnInit, OnDestroy {

  routeSub: any;

  constructor(
    private route: ActivatedRoute,
    private candidateProfilePicMgmtPopupService: ProfileMgmtPopupService,
    private dataService: DataStorageService
  ) {}

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      if (params['id']) {
        this.candidateProfilePicMgmtPopupService
          .open(ProfilePicMgmtPopupDialogComponent as Component, params['id']);
      } else {
        this.candidateProfilePicMgmtPopupService
          .open(ProfilePicMgmtPopupDialogComponent as Component, this.dataService.getData(USER_ID));
      }
    });
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
  }
}
