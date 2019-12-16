/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { QualificationDetailComponent } from 'app/entities/qualification/qualification-detail.component';
import { QualificationService } from 'app/entities/qualification/qualification.service';
import { Qualification } from 'app/entities/qualification/qualification.model';

describe('Component Tests', () => {
    describe('Qualification Management Detail Component', () => {
        let comp: QualificationDetailComponent;
        let fixture: ComponentFixture<QualificationDetailComponent>;
        let service: QualificationService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [QualificationDetailComponent],
                    providers: [QualificationService]
                })
                    .overrideTemplate(QualificationDetailComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(QualificationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(QualificationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: new Qualification(123)
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.qualification).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
