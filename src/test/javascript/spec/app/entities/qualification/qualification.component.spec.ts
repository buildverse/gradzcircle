/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { QualificationComponent } from 'app/entities/qualification/qualification.component';
import { QualificationService } from 'app/entities/qualification/qualification.service';
import { Qualification } from 'app/entities/qualification/qualification.model';

describe('Component Tests', () => {
    describe('Qualification Management Component', () => {
        let comp: QualificationComponent;
        let fixture: ComponentFixture<QualificationComponent>;
        let service: QualificationService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [QualificationComponent],
                    providers: [QualificationService]
                })
                    .overrideTemplate(QualificationComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(QualificationComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(QualificationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new Qualification(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.qualifications[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
