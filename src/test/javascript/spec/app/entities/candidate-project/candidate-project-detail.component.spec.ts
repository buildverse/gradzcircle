/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateProjectDetailComponent } from '../../../../../../main/webapp/app/entities/candidate-project/candidate-project-detail.component';
import { CandidateProjectService } from '../../../../../../main/webapp/app/entities/candidate-project/candidate-project.service';
import { CandidateProject } from '../../../../../../main/webapp/app/entities/candidate-project/candidate-project.model';

describe('Component Tests', () => {

    describe('CandidateProject Management Detail Component', () => {
        let comp: CandidateProjectDetailComponent;
        let fixture: ComponentFixture<CandidateProjectDetailComponent>;
        let service: CandidateProjectService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CandidateProjectDetailComponent],
                providers: [
                    CandidateProjectService
                ]
            })
            .overrideTemplate(CandidateProjectDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateProjectDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateProjectService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new CandidateProject(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.candidateProject).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
