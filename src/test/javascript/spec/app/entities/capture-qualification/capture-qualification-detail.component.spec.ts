/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { CaptureQualificationDetailComponent } from '../../../../../../main/webapp/app/entities/capture-qualification/capture-qualification-detail.component';
import { CaptureQualificationService } from '../../../../../../main/webapp/app/entities/capture-qualification/capture-qualification.service';
import { CaptureQualification } from '../../../../../../main/webapp/app/entities/capture-qualification/capture-qualification.model';

describe('Component Tests', () => {

    describe('CaptureQualification Management Detail Component', () => {
        let comp: CaptureQualificationDetailComponent;
        let fixture: ComponentFixture<CaptureQualificationDetailComponent>;
        let service: CaptureQualificationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CaptureQualificationDetailComponent],
                providers: [
                    CaptureQualificationService
                ]
            })
            .overrideTemplate(CaptureQualificationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CaptureQualificationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CaptureQualificationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new CaptureQualification(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.captureQualification).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
