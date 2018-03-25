import { Component, OnInit } from '@angular/core';
import { Principal } from '../../shared/auth/principal.service';
import { Candidate } from '../../entities/candidate/candidate.model';
import { CandidateService } from '../../entities/candidate/candidate.service';
import { Account } from '../../shared'
import 'rxjs/add/operator/debounceTime';
import { ActivatedRoute ,Router,ActivatedRouteSnapshot} from '@angular/router';


@Component({
    moduleId: module.id,
    selector: 'selector-name',
    templateUrl: 'candidate-about-me-details.component.html',
    styleUrls : ['candidate.css']
})

export class CandidateProfileAboutMeDetailsComponent implements OnInit {
    candidate: Candidate;
    //account: Account;
    errorMessage: String;
  
    constructor(
        private principal: Principal,
        private candidateService: CandidateService,
        private route: ActivatedRoute,
        private router: Router

    ) {}

    ngOnInit() { 
        this.candidate = this.route.snapshot.data['candidate'];
        this.route.parent.snapshot.data['candidate'] = this.candidate;
    }

    save ():void {
        this.router.navigate['candidate-profile/aboutMe'];
    }
}