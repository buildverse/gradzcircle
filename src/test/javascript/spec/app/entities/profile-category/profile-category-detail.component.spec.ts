/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GradzcircleTestModule } from '../../../test.module';
import { ProfileCategoryDetailComponent } from '../../../../../../main/webapp/app/entities/profile-category/profile-category-detail.component';
import { ProfileCategoryService } from '../../../../../../main/webapp/app/entities/profile-category/profile-category.service';
import { ProfileCategory } from '../../../../../../main/webapp/app/entities/profile-category/profile-category.model';

describe('Component Tests', () => {

    describe('ProfileCategory Management Detail Component', () => {
        let comp: ProfileCategoryDetailComponent;
        let fixture: ComponentFixture<ProfileCategoryDetailComponent>;
        let service: ProfileCategoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GradzcircleTestModule],
                declarations: [ProfileCategoryDetailComponent],
                providers: [
                    ProfileCategoryService
                ]
            })
            .overrideTemplate(ProfileCategoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProfileCategoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProfileCategoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ProfileCategory(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.profileCategory).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
