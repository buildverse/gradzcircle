/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { tick } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
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
            it('Should call load all with query method  when currentSearch not available', () => {
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

            it('Should call load all with search method with currentSearch ', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'search')
                    .and.callFake(function arg() {
                        return '123';
                    })
                    .and.returnValue(
                        Observable.of(
                            new HttpResponse({
                                body: [new AppConfig(123)],
                                headers
                            })
                        )
                    );

                // WHEN
                comp.currentSearch = '123';
                comp.ngOnInit();

                // THEN
                expect(service.search).toHaveBeenCalled();
                expect(comp.appConfigs.length).toEqual(1);
                expect(comp.appConfigs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });

            it('Should call load all with query method with currentSearch empty ', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new AppConfig(123, 'abhinav', false), new AppConfig(456, 'abhinav1', true)],
                            headers
                        })
                    )
                );
                // WHEN
                comp.currentSearch = undefined;
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.appConfigs.length).toEqual(2);
                expect(comp.appConfigs).toEqual(
                    jasmine.objectContaining([
                        { id: 123, configName: 'abhinav', configValue: false },
                        { id: 456, configName: 'abhinav1', configValue: true }
                    ])
                );
            });

            it('Should call search with from with currentSearch wwhen called from html  ', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'search')
                    .and.callFake(function arg() {
                        return '123';
                    })
                    .and.returnValue(
                        Observable.of(
                            new HttpResponse({
                                body: [new AppConfig(123)],
                                headers
                            })
                        )
                    );

                // WHEN
                comp.search('123');

                // THEN
                expect(service.search).toHaveBeenCalled();
                expect(comp.appConfigs.length).toEqual(1);
                expect(comp.appConfigs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
            });

            it('Should call load all with query method with currentSearch cleared by user ', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(
                    Observable.of(
                        new HttpResponse({
                            body: [new AppConfig(123, 'abhinav', false), new AppConfig(456, 'abhinav1', true)],
                            headers
                        })
                    )
                );
                // WHEN
                comp.clear();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.currentSearch).toBe('');
                expect(comp.appConfigs.length).toEqual(2);
                expect(comp.appConfigs).toEqual(
                    jasmine.objectContaining([
                        { id: 123, configName: 'abhinav', configValue: false },
                        { id: 456, configName: 'abhinav1', configValue: true }
                    ])
                );
            });
        });

        it('Should call load all with query method with currentSearch not defined by user ', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                Observable.of(
                    new HttpResponse({
                        body: [new AppConfig(123, 'abhinav', false), new AppConfig(456, 'abhinav1', true)],
                        headers
                    })
                )
            );
            // WHEN
            comp.search(undefined);

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.currentSearch).toBe('');
            expect(comp.appConfigs.length).toEqual(2);
            expect(comp.appConfigs).toEqual(
                jasmine.objectContaining([
                    { id: 123, configName: 'abhinav', configValue: false },
                    { id: 456, configName: 'abhinav1', configValue: true }
                ])
            );
        });
    });
});
