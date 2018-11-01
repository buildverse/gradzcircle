/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { CaptureCollegeDetailComponent } from '../../../../../../main/webapp/app/entities/capture-college/capture-college-detail.component';
import { CaptureCollegeService } from '../../../../../../main/webapp/app/entities/capture-college/capture-college.service';
import { CaptureCollege } from '../../../../../../main/webapp/app/entities/capture-college/capture-college.model';

describe('Component Tests', () => {

    describe('CaptureCollege Management Detail Component', () => {
        let comp: CaptureCollegeDetailComponent;
        let fixture: ComponentFixture<CaptureCollegeDetailComponent>;
        let service: CaptureCollegeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CaptureCollegeDetailComponent],
                providers: [
                    CaptureCollegeService
                ]
            })
            .overrideTemplate(CaptureCollegeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CaptureCollegeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CaptureCollegeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new CaptureCollege(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.captureCollege).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
