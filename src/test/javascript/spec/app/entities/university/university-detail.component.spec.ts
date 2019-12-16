/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { UniversityDetailComponent } from 'app/entities/university/university-detail.component';
import { UniversityService } from 'app/entities/university/university.service';
import { University } from 'app/entities/university/university.model';

describe('Component Tests', () => {
    describe('University Management Detail Component', () => {
        let comp: UniversityDetailComponent;
        let fixture: ComponentFixture<UniversityDetailComponent>;
        let service: UniversityService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [UniversityDetailComponent],
                    providers: [UniversityService]
                })
                    .overrideTemplate(UniversityDetailComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(UniversityDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UniversityService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: new University(123)
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.university).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
