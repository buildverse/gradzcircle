/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { CaptureUniversityDetailComponent } from '../../../../../../main/webapp/app/entities/capture-university/capture-university-detail.component';
import { CaptureUniversityService } from '../../../../../../main/webapp/app/entities/capture-university/capture-university.service';
import { CaptureUniversity } from '../../../../../../main/webapp/app/entities/capture-university/capture-university.model';

describe('Component Tests', () => {

    describe('CaptureUniversity Management Detail Component', () => {
        let comp: CaptureUniversityDetailComponent;
        let fixture: ComponentFixture<CaptureUniversityDetailComponent>;
        let service: CaptureUniversityService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CaptureUniversityDetailComponent],
                providers: [
                    CaptureUniversityService
                ]
            })
            .overrideTemplate(CaptureUniversityDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CaptureUniversityDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CaptureUniversityService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new CaptureUniversity(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.captureUniversity).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
