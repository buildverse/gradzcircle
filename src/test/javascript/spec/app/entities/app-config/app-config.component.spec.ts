/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { AppConfigComponent } from 'app/entities/app-config/app-config.component';
import { AppConfigService } from 'app/entities/app-config/app-config.service';
import { AppConfig } from 'app/entities/app-config/app-config.model';

describe('Component Tests', () => {
    describe('AppConfig Management Component', () => {
        let comp: AppConfigComponent;
        let fixture: ComponentFixture<AppConfigComponent>;
        let service: AppConfigService;

        beforeEach(
            async(() => {
                TestBed.configureTestingModule({
                    imports: [GradzcircleTestModule],
                    declarations: [AppConfigComponent],
                    providers: [AppConfigService]
                })
                    .overrideTemplate(AppConfigComponent, '')
                    .compileComponents();
            })
        );

        beforeEach(() => {
            fixture = TestBed.createComponent(AppConfigComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AppConfigService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new AppConfig(123)],
                            headers
                        })
                    )
                );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.appConfigs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
