import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('CaptureCourse e2e test', () => {

    let navBarPage: NavBarPage;
    let captureCourseDialogPage: CaptureCourseDialogPage;
    let captureCourseComponentsPage: CaptureCourseComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load CaptureCourses', () => {
        navBarPage.goToEntity('capture-course');
        captureCourseComponentsPage = new CaptureCourseComponentsPage();
        expect(captureCourseComponentsPage.getTitle())
            .toMatch(/gradzcircleApp.captureCourse.home.title/);

    });

    it('should load create CaptureCourse dialog', () => {
        captureCourseComponentsPage.clickOnCreateButton();
        captureCourseDialogPage = new CaptureCourseDialogPage();
        expect(captureCourseDialogPage.getModalTitle())
            .toMatch(/gradzcircleApp.captureCourse.home.createOrEditLabel/);
        captureCourseDialogPage.close();
    });

    it('should create and save CaptureCourses', () => {
        captureCourseComponentsPage.clickOnCreateButton();
        captureCourseDialogPage.setCourseNameInput('courseName');
        expect(captureCourseDialogPage.getCourseNameInput()).toMatch('courseName');
        captureCourseDialogPage.candidateEducationSelectLastOption();
        captureCourseDialogPage.save();
        expect(captureCourseDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CaptureCourseComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-capture-course div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CaptureCourseDialogPage {
    modalTitle = element(by.css('h4#myCaptureCourseLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    courseNameInput = element(by.css('input#field_courseName'));
    candidateEducationSelect = element(by.css('select#field_candidateEducation'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setCourseNameInput = function(courseName) {
        this.courseNameInput.sendKeys(courseName);
    };

    getCourseNameInput = function() {
        return this.courseNameInput.getAttribute('value');
    };

    candidateEducationSelectLastOption = function() {
        this.candidateEducationSelect.all(by.tagName('option')).last().click();
    };

    candidateEducationSelectOption = function(option) {
        this.candidateEducationSelect.sendKeys(option);
    };

    getCandidateEducationSelect = function() {
        return this.candidateEducationSelect;
    };

    getCandidateEducationSelectedOption = function() {
        return this.candidateEducationSelect.element(by.css('option:checked')).getText();
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
