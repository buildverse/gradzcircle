/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { GenderComponent } from 'app/entities/gender/gender.component';
import { GenderService } from 'app/entities/gender/gender.service';
import { Gender } from 'app/entities/gender/gender.model';

describe('Component Tests', () => {
    describe('Gender Management Component', () => {
        let comp: GenderComponent;
        let fixture: ComponentFixture<GenderComponent>;
        let service: GenderService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [GenderComponent],
                    providers: [GenderService]
                })
                    .overrideTemplate(GenderComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(GenderComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GenderService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new Gender(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.genders[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
