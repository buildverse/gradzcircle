import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('MaritalStatus e2e test', () => {

    let navBarPage: NavBarPage;
    let maritalStatusDialogPage: MaritalStatusDialogPage;
    let maritalStatusComponentsPage: MaritalStatusComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load MaritalStatuses', () => {
        navBarPage.goToEntity('marital-status');
        maritalStatusComponentsPage = new MaritalStatusComponentsPage();
        expect(maritalStatusComponentsPage.getTitle()).toMatch(/gradzcircleApp.maritalStatus.home.title/);

    });

    it('should load create MaritalStatus dialog', () => {
        maritalStatusComponentsPage.clickOnCreateButton();
        maritalStatusDialogPage = new MaritalStatusDialogPage();
        expect(maritalStatusDialogPage.getModalTitle()).toMatch(/gradzcircleApp.maritalStatus.home.createOrEditLabel/);
        maritalStatusDialogPage.close();
    });

    it('should create and save MaritalStatuses', () => {
        maritalStatusComponentsPage.clickOnCreateButton();
        maritalStatusDialogPage.setStatusInput('status');
        expect(maritalStatusDialogPage.getStatusInput()).toMatch('status');
        maritalStatusDialogPage.save();
        expect(maritalStatusDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class MaritalStatusComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-marital-status div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class MaritalStatusDialogPage {
    modalTitle = element(by.css('h4#myMaritalStatusLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    statusInput = element(by.css('input#field_status'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setStatusInput = function (status) {
        this.statusInput.sendKeys(status);
    }

    getStatusInput = function () {
        return this.statusInput.getAttribute('value');
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
