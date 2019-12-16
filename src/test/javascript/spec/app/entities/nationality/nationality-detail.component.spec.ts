/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { NationalityDetailComponent } from 'app/entities/nationality/nationality-detail.component';
import { NationalityService } from 'app/entities/nationality/nationality.service';
import { Nationality } from 'app/entities/nationality/nationality.model';

describe('Component Tests', () => {
    describe('Nationality Management Detail Component', () => {
        let comp: NationalityDetailComponent;
        let fixture: ComponentFixture<NationalityDetailComponent>;
        let service: NationalityService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [NationalityDetailComponent],
                    providers: [NationalityService]
                })
                    .overrideTemplate(NationalityDetailComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(NationalityDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NationalityService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: new Nationality(123)
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.nationality).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
