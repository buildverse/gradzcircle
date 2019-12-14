import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Skills } from './skills.model';
import { SkillsService } from './skills.service';

@Component({
    selector: 'jhi-skills-detail',
    templateUrl: './skills-detail.component.html'
})
export class SkillsDetailComponent implements OnInit, OnDestroy {
    skills: Skills;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(private eventManager: JhiEventManager, private skillsService: SkillsService, private route: ActivatedRoute) {}

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerChangeInSkills();
    }

    load(id) {
        this.skillsService.find(id).subscribe((skillsResponse: HttpResponse<Skills>) => {
            this.skills = skillsResponse.body;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSkills() {
        this.eventSubscriber = this.eventManager.subscribe('skillsListModification', response => this.load(this.skills.id));
    }
}
