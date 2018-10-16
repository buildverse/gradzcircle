import {Component, OnInit, OnChanges, Input} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Candidate} from '../../entities/candidate/candidate.model';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';
import {Subscription} from 'rxjs/Rx';
import {ITEMS_PER_PAGE, Principal, ResponseWrapper, UserService} from '../../shared';
import {LocalStorageService, SessionStorageService} from 'ng2-webstorage';
import {CandidateService} from '../../entities/candidate/candidate.service';
import {Observable} from 'rxjs/Observable';
import {SERVER_API_URL} from '../../app.constants';


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
  eventSubscriber: Subscription;
  currentSearch: string;
  defaultImage = require('../../../content/images/no-image.png');

  constructor(private route: ActivatedRoute,
    private eventManager: JhiEventManager,
    private alertService: JhiAlertService,
    private candidateService: CandidateService,
    private localStorage: LocalStorageService,
    private sessionStorage: SessionStorageService,
    private principal: Principal,
    private userService: UserService,
    private router: Router) {}


  ngOnInit() {
    this.route.data.subscribe((data: {candidate: Candidate}) => this.candidate = data.candidate);
    this.currentSearch = this.candidate.id.toString();
    console.log('candidate got is '+JSON.stringify(this.candidate));
    this.loginId = this.candidate.login.id;
    //this.loadCandidateDetail();
    // this.route.data.subscribe((data: {imageData: any })=> this.imageData = data.imageData);
    this.eventManager.broadcast({name: 'updateNavbarImage', content: 'OK'});
    this.reloadUserImage()
    // this.checkImageUrl();
    this.registerChangeInCandidateData();
    this.registerChangeInCandidateImage();
  }


  reloadCandidate() {
    this.candidateService.getCandidateByLoginId(this.loginId).subscribe(
      (res: Candidate) => {
        this.candidate = res;
      },
      (res: Error) => this.onError(res)
    );
    return;


  }

  // private checkImageUrl(){
  //     this.noImage = false;
  //     if(!this.imageData._body){
  //         this.noImage = true;
  //     }
  // }

  // reloadCandidateImage(){
  //     console.log("Image data id is "+ JSON.stringify(this.loginId));
  //     this.candidateService.getProfilePic(parseInt(this.loginId)).subscribe(response => {
  //         this.imageData = response;
  //         console.log("Image data is "+ JSON.stringify(this.imageData));
  //         this.checkImageUrl();
  //         this.imageData.url = this.imageData.url+'-'+Math.random().toString();
  //     });
  // }

  private onError(error) {
    this.alertService.error(error.message, null, null);
  }
  registerChangeInCandidateData() {
    this.eventSubscriber = this.eventManager.subscribe('candidateListModification', (response) => this.reloadCandidate());
  }

  registerChangeInCandidateImage() {
    this.eventSubscriber = this.eventManager.subscribe('candidateImageModification', (response) => this.reloadUserImage());
  }


  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  reloadUserImage() {
    this.noImage = false;
    this.principal.identity(true).then((user) => {
      if (user) {
        if (user.imageUrl) {
          this.userService.getImageData(user.id).subscribe((response) => {
            const responseJson = response.json()
            this.imageUrl = responseJson[0].href + '?t=' + Math.random().toString();
          }, (error: any) => {
            console.log(`${error}`);
            this.router.navigate(['/error']);
            return Observable.of(null);
          });
        } else {
          this.noImage = true;
        }
      }


    });
  }
}