import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('CaptureCollege e2e test', () => {

    let navBarPage: NavBarPage;
    let captureCollegeDialogPage: CaptureCollegeDialogPage;
    let captureCollegeComponentsPage: CaptureCollegeComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load CaptureColleges', () => {
        navBarPage.goToEntity('capture-college');
        captureCollegeComponentsPage = new CaptureCollegeComponentsPage();
        expect(captureCollegeComponentsPage.getTitle()).toMatch(/gradzcircleApp.captureCollege.home.title/);

    });

    it('should load create CaptureCollege dialog', () => {
        captureCollegeComponentsPage.clickOnCreateButton();
        captureCollegeDialogPage = new CaptureCollegeDialogPage();
        expect(captureCollegeDialogPage.getModalTitle()).toMatch(/gradzcircleApp.captureCollege.home.createOrEditLabel/);
        captureCollegeDialogPage.close();
    });

    it('should create and save CaptureColleges', () => {
        captureCollegeComponentsPage.clickOnCreateButton();
        captureCollegeDialogPage.setCollegeNameInput('collegeName');
        expect(captureCollegeDialogPage.getCollegeNameInput()).toMatch('collegeName');
        captureCollegeDialogPage.candidateEducationSelectLastOption();
        captureCollegeDialogPage.save();
        expect(captureCollegeDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CaptureCollegeComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-capture-college div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CaptureCollegeDialogPage {
    modalTitle = element(by.css('h4#myCaptureCollegeLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    collegeNameInput = element(by.css('input#field_collegeName'));
    candidateEducationSelect = element(by.css('select#field_candidateEducation'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setCollegeNameInput = function (collegeName) {
        this.collegeNameInput.sendKeys(collegeName);
    }

    getCollegeNameInput = function () {
        return this.collegeNameInput.getAttribute('value');
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
