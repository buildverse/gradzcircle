/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { LanguageComponent } from 'app/entities/language/language.component';
import { LanguageService } from 'app/entities/language/language.service';
import { Language } from 'app/entities/language/language.model';

describe('Component Tests', () => {
    describe('Language Management Component', () => {
        let comp: LanguageComponent;
        let fixture: ComponentFixture<LanguageComponent>;
        let service: LanguageService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [LanguageComponent],
                    providers: [LanguageService]
                })
                    .overrideTemplate(LanguageComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(LanguageComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LanguageService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new Language(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.languages[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
