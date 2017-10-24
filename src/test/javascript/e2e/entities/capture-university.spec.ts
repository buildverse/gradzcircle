import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('CaptureUniversity e2e test', () => {

    let navBarPage: NavBarPage;
    let captureUniversityDialogPage: CaptureUniversityDialogPage;
    let captureUniversityComponentsPage: CaptureUniversityComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load CaptureUniversities', () => {
        navBarPage.goToEntity('capture-university');
        captureUniversityComponentsPage = new CaptureUniversityComponentsPage();
        expect(captureUniversityComponentsPage.getTitle()).toMatch(/gradzcircleApp.captureUniversity.home.title/);

    });

    it('should load create CaptureUniversity dialog', () => {
        captureUniversityComponentsPage.clickOnCreateButton();
        captureUniversityDialogPage = new CaptureUniversityDialogPage();
        expect(captureUniversityDialogPage.getModalTitle()).toMatch(/gradzcircleApp.captureUniversity.home.createOrEditLabel/);
        captureUniversityDialogPage.close();
    });

    it('should create and save CaptureUniversities', () => {
        captureUniversityComponentsPage.clickOnCreateButton();
        captureUniversityDialogPage.setUniversityNameInput('universityName');
        expect(captureUniversityDialogPage.getUniversityNameInput()).toMatch('universityName');
        captureUniversityDialogPage.capturecollegeSelectLastOption();
        captureUniversityDialogPage.save();
        expect(captureUniversityDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CaptureUniversityComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-capture-university div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CaptureUniversityDialogPage {
    modalTitle = element(by.css('h4#myCaptureUniversityLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    universityNameInput = element(by.css('input#field_universityName'));
    capturecollegeSelect = element(by.css('select#field_capturecollege'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setUniversityNameInput = function (universityName) {
        this.universityNameInput.sendKeys(universityName);
    }

    getUniversityNameInput = function () {
        return this.universityNameInput.getAttribute('value');
    }

    capturecollegeSelectLastOption = function () {
        this.capturecollegeSelect.all(by.tagName('option')).last().click();
    }

    capturecollegeSelectOption = function (option) {
        this.capturecollegeSelect.sendKeys(option);
    }

    getCapturecollegeSelect = function () {
        return this.capturecollegeSelect;
    }

    getCapturecollegeSelectedOption = function () {
        return this.capturecollegeSelect.element(by.css('option:checked')).getText();
    }

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
