import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Qualification e2e test', () => {

    let navBarPage: NavBarPage;
    let qualificationDialogPage: QualificationDialogPage;
    let qualificationComponentsPage: QualificationComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Qualifications', () => {
        navBarPage.goToEntity('qualification');
        qualificationComponentsPage = new QualificationComponentsPage();
        expect(qualificationComponentsPage.getTitle()).toMatch(/gradzcircleApp.qualification.home.title/);

    });

    it('should load create Qualification dialog', () => {
        qualificationComponentsPage.clickOnCreateButton();
        qualificationDialogPage = new QualificationDialogPage();
        expect(qualificationDialogPage.getModalTitle()).toMatch(/gradzcircleApp.qualification.home.createOrEditLabel/);
        qualificationDialogPage.close();
    });

    it('should create and save Qualifications', () => {
        qualificationComponentsPage.clickOnCreateButton();
        qualificationDialogPage.setQualificationInput('qualification');
        expect(qualificationDialogPage.getQualificationInput()).toMatch('qualification');
        qualificationDialogPage.save();
        expect(qualificationDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class QualificationComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-qualification div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class QualificationDialogPage {
    modalTitle = element(by.css('h4#myQualificationLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    qualificationInput = element(by.css('input#field_qualification'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setQualificationInput = function (qualification) {
        this.qualificationInput.sendKeys(qualification);
    }

    getQualificationInput = function () {
        return this.qualificationInput.getAttribute('value');
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
