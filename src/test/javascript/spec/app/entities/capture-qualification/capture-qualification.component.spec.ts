/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { CaptureQualificationComponent } from '../../../../../../main/webapp/app/entities/capture-qualification/capture-qualification.component';
import { CaptureQualificationService } from '../../../../../../main/webapp/app/entities/capture-qualification/capture-qualification.service';
import { CaptureQualification } from '../../../../../../main/webapp/app/entities/capture-qualification/capture-qualification.model';

describe('Component Tests', () => {

    describe('CaptureQualification Management Component', () => {
        let comp: CaptureQualificationComponent;
        let fixture: ComponentFixture<CaptureQualificationComponent>;
        let service: CaptureQualificationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CaptureQualificationComponent],
                providers: [
                    CaptureQualificationService
                ]
            })
            .overrideTemplate(CaptureQualificationComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CaptureQualificationComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CaptureQualificationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new CaptureQualification(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.captureQualifications[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
