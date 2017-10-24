import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('CaptureQualification e2e test', () => {

    let navBarPage: NavBarPage;
    let captureQualificationDialogPage: CaptureQualificationDialogPage;
    let captureQualificationComponentsPage: CaptureQualificationComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load CaptureQualifications', () => {
        navBarPage.goToEntity('capture-qualification');
        captureQualificationComponentsPage = new CaptureQualificationComponentsPage();
        expect(captureQualificationComponentsPage.getTitle()).toMatch(/gradzcircleApp.captureQualification.home.title/);

    });

    it('should load create CaptureQualification dialog', () => {
        captureQualificationComponentsPage.clickOnCreateButton();
        captureQualificationDialogPage = new CaptureQualificationDialogPage();
        expect(captureQualificationDialogPage.getModalTitle()).toMatch(/gradzcircleApp.captureQualification.home.createOrEditLabel/);
        captureQualificationDialogPage.close();
    });

    it('should create and save CaptureQualifications', () => {
        captureQualificationComponentsPage.clickOnCreateButton();
        captureQualificationDialogPage.setQualificationNameInput('qualificationName');
        expect(captureQualificationDialogPage.getQualificationNameInput()).toMatch('qualificationName');
        captureQualificationDialogPage.candidateEducationSelectLastOption();
        captureQualificationDialogPage.save();
        expect(captureQualificationDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CaptureQualificationComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-capture-qualification div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CaptureQualificationDialogPage {
    modalTitle = element(by.css('h4#myCaptureQualificationLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    qualificationNameInput = element(by.css('input#field_qualificationName'));
    candidateEducationSelect = element(by.css('select#field_candidateEducation'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setQualificationNameInput = function (qualificationName) {
        this.qualificationNameInput.sendKeys(qualificationName);
    }

    getQualificationNameInput = function () {
        return this.qualificationNameInput.getAttribute('value');
    }

    candidateEducationSelectLastOption = function () {
        this.candidateEducationSelect.all(by.tagName('option')).last().click();
    }

    candidateEducationSelectOption = function (option) {
        this.candidateEducationSelect.sendKeys(option);
    }

    getCandidateEducationSelect = function () {
        return this.candidateEducationSelect;
    }

    getCandidateEducationSelectedOption = function () {
        return this.candidateEducationSelect.element(by.css('option:checked')).getText();
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
