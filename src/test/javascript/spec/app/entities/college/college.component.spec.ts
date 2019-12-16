/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { CollegeComponent } from 'app/entities/college/college.component';
import { CollegeService } from 'app/entities/college/college.service';
import { College } from 'app/entities/college/college.model';

describe('Component Tests', () => {
    describe('College Management Component', () => {
        let comp: CollegeComponent;
        let fixture: ComponentFixture<CollegeComponent>;
        let service: CollegeService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [CollegeComponent],
                    providers: [CollegeService]
                })
                    .overrideTemplate(CollegeComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(CollegeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CollegeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new College(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.colleges[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
