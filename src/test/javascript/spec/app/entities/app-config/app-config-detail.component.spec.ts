/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { AppConfigDetailComponent } from 'app/entities/app-config/app-config-detail.component';
import { AppConfigService } from 'app/entities/app-config/app-config.service';
import { AppConfig } from 'app/entities/app-config/app-config.model';

describe('Component Tests', () => {
    describe('AppConfig Management Detail Component', () => {
        let comp: AppConfigDetailComponent;
        let fixture: ComponentFixture<AppConfigDetailComponent>;
        let service: AppConfigService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [AppConfigDetailComponent],
                    providers: [AppConfigService]
                })
                    .overrideTemplate(AppConfigDetailComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(AppConfigDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AppConfigService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: new AppConfig(123)
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.appConfig).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
