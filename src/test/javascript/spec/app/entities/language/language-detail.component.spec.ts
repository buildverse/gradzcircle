/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { LanguageDetailComponent } from '../../../../../../main/webapp/app/entities/language/language-detail.component';
import { LanguageService } from '../../../../../../main/webapp/app/entities/language/language.service';
import { Language } from '../../../../../../main/webapp/app/entities/language/language.model';

describe('Component Tests', () => {

    describe('Language Management Detail Component', () => {
        let comp: LanguageDetailComponent;
        let fixture: ComponentFixture<LanguageDetailComponent>;
        let service: LanguageService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [LanguageDetailComponent],
                providers: [
                    LanguageService
                ]
            })
            .overrideTemplate(LanguageDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LanguageDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LanguageService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Language(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.language).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
