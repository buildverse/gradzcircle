/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { CollegeDetailComponent } from '../../../../../../main/webapp/app/entities/college/college-detail.component';
import { CollegeService } from '../../../../../../main/webapp/app/entities/college/college.service';
import { College } from '../../../../../../main/webapp/app/entities/college/college.model';

describe('Component Tests', () => {

    describe('College Management Detail Component', () => {
        let comp: CollegeDetailComponent;
        let fixture: ComponentFixture<CollegeDetailComponent>;
        let service: CollegeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [CollegeDetailComponent],
                providers: [
                    CollegeService
                ]
            })
            .overrideTemplate(CollegeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CollegeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CollegeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new College(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.college).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
