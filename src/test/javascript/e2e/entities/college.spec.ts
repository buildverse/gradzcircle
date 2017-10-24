import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('College e2e test', () => {

    let navBarPage: NavBarPage;
    let collegeDialogPage: CollegeDialogPage;
    let collegeComponentsPage: CollegeComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Colleges', () => {
        navBarPage.goToEntity('college');
        collegeComponentsPage = new CollegeComponentsPage();
        expect(collegeComponentsPage.getTitle()).toMatch(/gradzcircleApp.college.home.title/);

    });

    it('should load create College dialog', () => {
        collegeComponentsPage.clickOnCreateButton();
        collegeDialogPage = new CollegeDialogPage();
        expect(collegeDialogPage.getModalTitle()).toMatch(/gradzcircleApp.college.home.createOrEditLabel/);
        collegeDialogPage.close();
    });

    it('should create and save Colleges', () => {
        collegeComponentsPage.clickOnCreateButton();
        collegeDialogPage.setCollegeNameInput('collegeName');
        expect(collegeDialogPage.getCollegeNameInput()).toMatch('collegeName');
        collegeDialogPage.setDomainNameInput('domainName');
        expect(collegeDialogPage.getDomainNameInput()).toMatch('domainName');
        collegeDialogPage.setStatusInput('5');
        expect(collegeDialogPage.getStatusInput()).toMatch('5');
        collegeDialogPage.universitySelectLastOption();
        collegeDialogPage.save();
        expect(collegeDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CollegeComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-college div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CollegeDialogPage {
    modalTitle = element(by.css('h4#myCollegeLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    collegeNameInput = element(by.css('input#field_collegeName'));
    domainNameInput = element(by.css('input#field_domainName'));
    statusInput = element(by.css('input#field_status'));
    universitySelect = element(by.css('select#field_university'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setCollegeNameInput = function (collegeName) {
        this.collegeNameInput.sendKeys(collegeName);
    }

    getCollegeNameInput = function () {
        return this.collegeNameInput.getAttribute('value');
    }

    setDomainNameInput = function (domainName) {
        this.domainNameInput.sendKeys(domainName);
    }

    getDomainNameInput = function () {
        return this.domainNameInput.getAttribute('value');
    }

    setStatusInput = function (status) {
        this.statusInput.sendKeys(status);
    }

    getStatusInput = function () {
        return this.statusInput.getAttribute('value');
    }

    universitySelectLastOption = function () {
        this.universitySelect.all(by.tagName('option')).last().click();
    }

    universitySelectOption = function (option) {
        this.universitySelect.sendKeys(option);
    }

    getUniversitySelect = function () {
        return this.universitySelect;
    }

    getUniversitySelectedOption = function () {
        return this.universitySelect.element(by.css('option:checked')).getText();
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
