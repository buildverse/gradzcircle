/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { CandidateComponent } from '../../../../../../main/webapp/app/entities/candidate/candidate.component';
import { CandidateService } from '../../../../../../main/webapp/app/entities/candidate/candidate.service';
import { Candidate } from '../../../../../../main/webapp/app/entities/candidate/candidate.model';

describe('Component Tests', () => {

    describe('Candidate Management Component', () => {
        let comp: CandidateComponent;
        let fixture: ComponentFixture<CandidateComponent>;
        let service: CandidateService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CandidateComponent],
                providers: [
                    CandidateService
                ]
            })
            .overrideTemplate(CandidateComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CandidateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CandidateService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Candidate(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.candidates[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
