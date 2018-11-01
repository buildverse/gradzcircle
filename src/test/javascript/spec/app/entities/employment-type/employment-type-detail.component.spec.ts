/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { EmploymentTypeDetailComponent } from '../../../../../../main/webapp/app/entities/employment-type/employment-type-detail.component';
import { EmploymentTypeService } from '../../../../../../main/webapp/app/entities/employment-type/employment-type.service';
import { EmploymentType } from '../../../../../../main/webapp/app/entities/employment-type/employment-type.model';

describe('Component Tests', () => {

    describe('EmploymentType Management Detail Component', () => {
        let comp: EmploymentTypeDetailComponent;
        let fixture: ComponentFixture<EmploymentTypeDetailComponent>;
        let service: EmploymentTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [EmploymentTypeDetailComponent],
                providers: [
                    EmploymentTypeService
                ]
            })
            .overrideTemplate(EmploymentTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EmploymentTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EmploymentTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new EmploymentType(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.employmentType).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
