/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradzcircleTestModule } from '../../../test.module';
import { ProfileCategoryComponent } from '../../../../../../main/webapp/app/entities/profile-category/profile-category.component';
import { ProfileCategoryService } from '../../../../../../main/webapp/app/entities/profile-category/profile-category.service';
import { ProfileCategory } from '../../../../../../main/webapp/app/entities/profile-category/profile-category.model';

describe('Component Tests', () => {

    describe('ProfileCategory Management Component', () => {
        let comp: ProfileCategoryComponent;
        let fixture: ComponentFixture<ProfileCategoryComponent>;
        let service: ProfileCategoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [ProfileCategoryComponent],
                providers: [
                    ProfileCategoryService
                ]
            })
            .overrideTemplate(ProfileCategoryComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProfileCategoryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProfileCategoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new ProfileCategory(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.profileCategories[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
