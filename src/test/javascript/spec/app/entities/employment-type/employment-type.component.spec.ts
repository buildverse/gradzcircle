/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { EmploymentTypeComponent } from '../../../../../../main/webapp/app/entities/employment-type/employment-type.component';
import { EmploymentTypeService } from '../../../../../../main/webapp/app/entities/employment-type/employment-type.service';
import { EmploymentType } from '../../../../../../main/webapp/app/entities/employment-type/employment-type.model';

describe('Component Tests', () => {

    describe('EmploymentType Management Component', () => {
        let comp: EmploymentTypeComponent;
        let fixture: ComponentFixture<EmploymentTypeComponent>;
        let service: EmploymentTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [EmploymentTypeComponent],
                providers: [
                    EmploymentTypeService
                ]
            })
            .overrideTemplate(EmploymentTypeComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EmploymentTypeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EmploymentTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new EmploymentType(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.employmentTypes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
