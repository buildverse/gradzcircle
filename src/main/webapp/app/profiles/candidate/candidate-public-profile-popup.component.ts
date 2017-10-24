import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute,Router } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { Principal,UserService } from '../../shared';
import { Candidate } from '../../entities/candidate/candidate.model';
import { CandidateService } from '../../entities/candidate/candidate.service';
import { CandidatePublicProfilePopupService } from './candidate-public-profile-popup.service';
import { Observable } from 'rxjs/Observable';

@Component({
    selector: 'jhi-candidate-public-profile-dialog',
    templateUrl: './candidate-public-profile.html',
    styleUrls: ['candidate.css']
})
export class CandidatePublicProfilePopupDialogComponent implements OnInit {

    candidate: Candidate;
    defaultImage = require("../../../content/images/no-image.png");
    userImage : any;
    noImage:boolean;
  
    constructor(
        private candidateService: CandidateService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private principal : Principal,
        private userService : UserService,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.reloadUserImage();
    }

    reloadUserImage(){
        this.noImage = false;
        this.principal.identity().then((user)=>{
            if(user){
                if(user.imageUrl){
                    this.userService.getImageData(user.id).subscribe(response =>{
                        let responseJson = response.json()
                        this.userImage = responseJson[0].href+'?t='+Math.random().toString();
                    },(error: any )=> {
                        console.log (`${error}`);
                        this.router.navigate(['/error']);
                        return Observable.of(null);
                    });
                }
                else
                    this.noImage = true;
            }    
        });     
    }

    private getWidth(candidateLanguageProficiency){
        if(candidateLanguageProficiency==='Beginner')
            return '33%';
        else if(candidateLanguageProficiency==='Intermediate')
            return '66%';
        else if (candidateLanguageProficiency==='Expert')
            return '100%';
    }

    // private checkImageData(){
    //     this.noImage = false;
    //     if(!this.imageData._body){
    //         this.noImage = true;
    //     }
    // }

 

    clear() {
        this.activeModal.dismiss('cancel');
    }
}

@Component({
    selector: 'jhi-candidate-public-profile-popup',
    template: ''
})
export class CandidatePublicProfilePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidatePublicProfilePopupService: CandidatePublicProfilePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.candidatePublicProfilePopupService
                .open(CandidatePublicProfilePopupDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
