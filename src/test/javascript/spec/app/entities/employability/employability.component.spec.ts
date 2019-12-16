/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { EmployabilityComponent } from 'app/entities/employability/employability.component';
import { EmployabilityService } from 'app/entities/employability/employability.service';
import { Employability } from 'app/entities/employability/employability.model';

describe('Component Tests', () => {
    describe('Employability Management Component', () => {
        let comp: EmployabilityComponent;
        let fixture: ComponentFixture<EmployabilityComponent>;
        let service: EmployabilityService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [EmployabilityComponent],
                    providers: [EmployabilityService]
                })
                    .overrideTemplate(EmployabilityComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(EmployabilityComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EmployabilityService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new Employability(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.employabilities[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
