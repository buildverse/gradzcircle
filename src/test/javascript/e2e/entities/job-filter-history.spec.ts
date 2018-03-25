import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('JobFilterHistory e2e test', () => {

    let navBarPage: NavBarPage;
    let jobFilterHistoryDialogPage: JobFilterHistoryDialogPage;
    let jobFilterHistoryComponentsPage: JobFilterHistoryComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load JobFilterHistories', () => {
        navBarPage.goToEntity('job-filter-history');
        jobFilterHistoryComponentsPage = new JobFilterHistoryComponentsPage();
        expect(jobFilterHistoryComponentsPage.getTitle()).toMatch(/gradzcircleApp.jobFilterHistory.home.title/);

    });

    it('should load create JobFilterHistory dialog', () => {
        jobFilterHistoryComponentsPage.clickOnCreateButton();
        jobFilterHistoryDialogPage = new JobFilterHistoryDialogPage();
        expect(jobFilterHistoryDialogPage.getModalTitle()).toMatch(/gradzcircleApp.jobFilterHistory.home.createOrEditLabel/);
        jobFilterHistoryDialogPage.close();
    });

    it('should create and save JobFilterHistories', () => {
        jobFilterHistoryComponentsPage.clickOnCreateButton();
        jobFilterHistoryDialogPage.setFilterDescriptionInput('filterDescription');
        expect(jobFilterHistoryDialogPage.getFilterDescriptionInput()).toMatch('filterDescription');
        jobFilterHistoryDialogPage.jobFilterSelectLastOption();
        jobFilterHistoryDialogPage.save();
        expect(jobFilterHistoryDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class JobFilterHistoryComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-job-filter-history div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class JobFilterHistoryDialogPage {
    modalTitle = element(by.css('h4#myJobFilterHistoryLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    filterDescriptionInput = element(by.css('input#field_filterDescription'));
    jobFilterSelect = element(by.css('select#field_jobFilter'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setFilterDescriptionInput = function (filterDescription) {
        this.filterDescriptionInput.sendKeys(filterDescription);
    }

    getFilterDescriptionInput = function () {
        return this.filterDescriptionInput.getAttribute('value');
    }

    jobFilterSelectLastOption = function () {
        this.jobFilterSelect.all(by.tagName('option')).last().click();
    }

    jobFilterSelectOption = function (option) {
        this.jobFilterSelect.sendKeys(option);
    }

    getJobFilterSelect = function () {
        return this.jobFilterSelect;
    }

    getJobFilterSelectedOption = function () {
        return this.jobFilterSelect.element(by.css('option:checked')).getText();
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
