import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Course e2e test', () => {

    let navBarPage: NavBarPage;
    let courseDialogPage: CourseDialogPage;
    let courseComponentsPage: CourseComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Courses', () => {
        navBarPage.goToEntity('course');
        courseComponentsPage = new CourseComponentsPage();
        expect(courseComponentsPage.getTitle())
            .toMatch(/gradzcircleApp.course.home.title/);

    });

    it('should load create Course dialog', () => {
        courseComponentsPage.clickOnCreateButton();
        courseDialogPage = new CourseDialogPage();
        expect(courseDialogPage.getModalTitle())
            .toMatch(/gradzcircleApp.course.home.createOrEditLabel/);
        courseDialogPage.close();
    });

    it('should create and save Courses', () => {
        courseComponentsPage.clickOnCreateButton();
        courseDialogPage.setCourseInput('course');
        expect(courseDialogPage.getCourseInput()).toMatch('course');
        courseDialogPage.save();
        expect(courseDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CourseComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-course div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CourseDialogPage {
    modalTitle = element(by.css('h4#myCourseLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    courseInput = element(by.css('input#field_course'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setCourseInput = function(course) {
        this.courseInput.sendKeys(course);
    };

    getCourseInput = function() {
        return this.courseInput.getAttribute('value');
    };

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
