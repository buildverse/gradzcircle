/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { CaptureUniversityComponent } from '../../../../../../main/webapp/app/entities/capture-university/capture-university.component';
import { CaptureUniversityService } from '../../../../../../main/webapp/app/entities/capture-university/capture-university.service';
import { CaptureUniversity } from '../../../../../../main/webapp/app/entities/capture-university/capture-university.model';

describe('Component Tests', () => {

    describe('CaptureUniversity Management Component', () => {
        let comp: CaptureUniversityComponent;
        let fixture: ComponentFixture<CaptureUniversityComponent>;
        let service: CaptureUniversityService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CaptureUniversityComponent],
                providers: [
                    CaptureUniversityService
                ]
            })
            .overrideTemplate(CaptureUniversityComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CaptureUniversityComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CaptureUniversityService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new CaptureUniversity(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.captureUniversities[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
