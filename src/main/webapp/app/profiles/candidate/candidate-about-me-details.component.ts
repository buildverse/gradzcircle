import { Component, OnInit } from '@angular/core';
import { Candidate } from '../../entities/candidate/candidate.model';
import 'rxjs/add/operator/debounceTime';
import { ActivatedRoute} from '@angular/router';


@Component({
    selector: 'jhi-candidate-details',
    templateUrl: 'candidate-about-me-details.component.html',
    styleUrls : ['candidate.css']
})

export class CandidateProfileAboutMeDetailsComponent implements OnInit {

    candidate: Candidate;

    errorMessage: String;
    constructor(
        private route: ActivatedRoute,
    ) {}

    ngOnInit() { 
        this.candidate = this.route.snapshot.data['candidate'].body;
    }
}