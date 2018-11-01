/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { SkillsComponent } from '../../../../../../main/webapp/app/entities/skills/skills.component';
import { SkillsService } from '../../../../../../main/webapp/app/entities/skills/skills.service';
import { Skills } from '../../../../../../main/webapp/app/entities/skills/skills.model';

describe('Component Tests', () => {

    describe('Skills Management Component', () => {
        let comp: SkillsComponent;
        let fixture: ComponentFixture<SkillsComponent>;
        let service: SkillsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [SkillsComponent],
                providers: [
                    SkillsService
                ]
            })
            .overrideTemplate(SkillsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SkillsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SkillsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Skills(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.skills[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
