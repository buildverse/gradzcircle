/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
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
                    SkillsService
                ]
            })
            .overrideTemplate(SkillsDetailComponent, '')
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

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Skills(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.skills).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
