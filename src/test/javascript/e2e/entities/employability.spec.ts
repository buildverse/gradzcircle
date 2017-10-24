import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Employability e2e test', () => {

    let navBarPage: NavBarPage;
    let employabilityDialogPage: EmployabilityDialogPage;
    let employabilityComponentsPage: EmployabilityComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Employabilities', () => {
        navBarPage.goToEntity('employability');
        employabilityComponentsPage = new EmployabilityComponentsPage();
        expect(employabilityComponentsPage.getTitle()).toMatch(/gradzcircleApp.employability.home.title/);

    });

    it('should load create Employability dialog', () => {
        employabilityComponentsPage.clickOnCreateButton();
        employabilityDialogPage = new EmployabilityDialogPage();
        expect(employabilityDialogPage.getModalTitle()).toMatch(/gradzcircleApp.employability.home.createOrEditLabel/);
        employabilityDialogPage.close();
    });

    it('should create and save Employabilities', () => {
        employabilityComponentsPage.clickOnCreateButton();
        employabilityDialogPage.setEmployableSkillNameInput('employableSkillName');
        expect(employabilityDialogPage.getEmployableSkillNameInput()).toMatch('employableSkillName');
        employabilityDialogPage.setEmployabilityScoreInput('5');
        expect(employabilityDialogPage.getEmployabilityScoreInput()).toMatch('5');
        employabilityDialogPage.setEmployabilityPercentileInput('5');
        expect(employabilityDialogPage.getEmployabilityPercentileInput()).toMatch('5');
        employabilityDialogPage.save();
        expect(employabilityDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class EmployabilityComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-employability div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class EmployabilityDialogPage {
    modalTitle = element(by.css('h4#myEmployabilityLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    employableSkillNameInput = element(by.css('input#field_employableSkillName'));
    employabilityScoreInput = element(by.css('input#field_employabilityScore'));
    employabilityPercentileInput = element(by.css('input#field_employabilityPercentile'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setEmployableSkillNameInput = function (employableSkillName) {
        this.employableSkillNameInput.sendKeys(employableSkillName);
    }

    getEmployableSkillNameInput = function () {
        return this.employableSkillNameInput.getAttribute('value');
    }

    setEmployabilityScoreInput = function (employabilityScore) {
        this.employabilityScoreInput.sendKeys(employabilityScore);
    }

    getEmployabilityScoreInput = function () {
        return this.employabilityScoreInput.getAttribute('value');
    }

    setEmployabilityPercentileInput = function (employabilityPercentile) {
        this.employabilityPercentileInput.sendKeys(employabilityPercentile);
    }

    getEmployabilityPercentileInput = function () {
        return this.employabilityPercentileInput.getAttribute('value');
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
