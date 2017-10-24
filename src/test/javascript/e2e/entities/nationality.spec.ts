import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Nationality e2e test', () => {

    let navBarPage: NavBarPage;
    let nationalityDialogPage: NationalityDialogPage;
    let nationalityComponentsPage: NationalityComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Nationalities', () => {
        navBarPage.goToEntity('nationality');
        nationalityComponentsPage = new NationalityComponentsPage();
        expect(nationalityComponentsPage.getTitle()).toMatch(/gradzcircleApp.nationality.home.title/);

    });

    it('should load create Nationality dialog', () => {
        nationalityComponentsPage.clickOnCreateButton();
        nationalityDialogPage = new NationalityDialogPage();
        expect(nationalityDialogPage.getModalTitle()).toMatch(/gradzcircleApp.nationality.home.createOrEditLabel/);
        nationalityDialogPage.close();
    });

    it('should create and save Nationalities', () => {
        nationalityComponentsPage.clickOnCreateButton();
        nationalityDialogPage.setNationalityInput('nationality');
        expect(nationalityDialogPage.getNationalityInput()).toMatch('nationality');
        nationalityDialogPage.save();
        expect(nationalityDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class NationalityComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-nationality div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class NationalityDialogPage {
    modalTitle = element(by.css('h4#myNationalityLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nationalityInput = element(by.css('input#field_nationality'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNationalityInput = function (nationality) {
        this.nationalityInput.sendKeys(nationality);
    }

    getNationalityInput = function () {
        return this.nationalityInput.getAttribute('value');
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
