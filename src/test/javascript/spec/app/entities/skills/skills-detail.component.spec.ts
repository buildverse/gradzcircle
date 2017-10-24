/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GradzcircleTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SkillsDetailComponent } from '../../../../../../main/webapp/app/entities/skills/skills-detail.component';
import { SkillsService } from '../../../../../../main/webapp/app/entities/skills/skills.service';
import { Skills } from '../../../../../../main/webapp/app/entities/skills/skills.model';

describe('Component Tests', () => {

    describe('Skills Management Detail Component', () => {
        let comp: SkillsDetailComponent;
        let fixture: ComponentFixture<SkillsDetailComponent>;
        let service: SkillsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [SkillsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SkillsService,
                    JhiEventManager
                ]
            }).overrideTemplate(SkillsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SkillsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SkillsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Skills(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.skills).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
