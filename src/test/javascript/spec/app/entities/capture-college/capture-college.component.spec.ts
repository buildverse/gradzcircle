/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { CaptureCollegeComponent } from '../../../../../../main/webapp/app/entities/capture-college/capture-college.component';
import { CaptureCollegeService } from '../../../../../../main/webapp/app/entities/capture-college/capture-college.service';
import { CaptureCollege } from '../../../../../../main/webapp/app/entities/capture-college/capture-college.model';

describe('Component Tests', () => {

    describe('CaptureCollege Management Component', () => {
        let comp: CaptureCollegeComponent;
        let fixture: ComponentFixture<CaptureCollegeComponent>;
        let service: CaptureCollegeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CaptureCollegeComponent],
                providers: [
                    CaptureCollegeService
                ]
            })
            .overrideTemplate(CaptureCollegeComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CaptureCollegeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CaptureCollegeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new CaptureCollege(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.captureColleges[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
