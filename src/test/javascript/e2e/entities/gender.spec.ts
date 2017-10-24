import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Gender e2e test', () => {

    let navBarPage: NavBarPage;
    let genderDialogPage: GenderDialogPage;
    let genderComponentsPage: GenderComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Genders', () => {
        navBarPage.goToEntity('gender');
        genderComponentsPage = new GenderComponentsPage();
        expect(genderComponentsPage.getTitle()).toMatch(/gradzcircleApp.gender.home.title/);

    });

    it('should load create Gender dialog', () => {
        genderComponentsPage.clickOnCreateButton();
        genderDialogPage = new GenderDialogPage();
        expect(genderDialogPage.getModalTitle()).toMatch(/gradzcircleApp.gender.home.createOrEditLabel/);
        genderDialogPage.close();
    });

    it('should create and save Genders', () => {
        genderComponentsPage.clickOnCreateButton();
        genderDialogPage.setGenderInput('gender');
        expect(genderDialogPage.getGenderInput()).toMatch('gender');
        genderDialogPage.save();
        expect(genderDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class GenderComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-gender div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class GenderDialogPage {
    modalTitle = element(by.css('h4#myGenderLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    genderInput = element(by.css('input#field_gender'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setGenderInput = function (gender) {
        this.genderInput.sendKeys(gender);
    }

    getGenderInput = function () {
        return this.genderInput.getAttribute('value');
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
