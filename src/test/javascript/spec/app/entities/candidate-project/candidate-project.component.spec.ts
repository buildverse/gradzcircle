/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateProjectComponent } from '../../../../../../main/webapp/app/entities/candidate-project/candidate-project.component';
import { CandidateProjectService } from '../../../../../../main/webapp/app/entities/candidate-project/candidate-project.service';
import { CandidateProject } from '../../../../../../main/webapp/app/entities/candidate-project/candidate-project.model';

describe('Component Tests', () => {

    describe('CandidateProject Management Component', () => {
        let comp: CandidateProjectComponent;
        let fixture: ComponentFixture<CandidateProjectComponent>;
        let service: CandidateProjectService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CandidateProjectComponent],
                providers: [
                    CandidateProjectService
                ]
            })
            .overrideTemplate(CandidateProjectComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateProjectComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateProjectService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new CandidateProject(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.candidateProjects[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
