import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('JobFilter e2e test', () => {

    let navBarPage: NavBarPage;
    let jobFilterDialogPage: JobFilterDialogPage;
    let jobFilterComponentsPage: JobFilterComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load JobFilters', () => {
        navBarPage.goToEntity('job-filter');
        jobFilterComponentsPage = new JobFilterComponentsPage();
        expect(jobFilterComponentsPage.getTitle())
            .toMatch(/gradzcircleApp.jobFilter.home.title/);

    });

    it('should load create JobFilter dialog', () => {
        jobFilterComponentsPage.clickOnCreateButton();
        jobFilterDialogPage = new JobFilterDialogPage();
        expect(jobFilterDialogPage.getModalTitle())
            .toMatch(/gradzcircleApp.jobFilter.home.createOrEditLabel/);
        jobFilterDialogPage.close();
    });

    it('should create and save JobFilters', () => {
        jobFilterComponentsPage.clickOnCreateButton();
        jobFilterDialogPage.setFilterDescriptionInput('filterDescription');
        expect(jobFilterDialogPage.getFilterDescriptionInput()).toMatch('filterDescription');
        jobFilterDialogPage.jobSelectLastOption();
        jobFilterDialogPage.save();
        expect(jobFilterDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class JobFilterComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-job-filter div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class JobFilterDialogPage {
    modalTitle = element(by.css('h4#myJobFilterLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    filterDescriptionInput = element(by.css('input#field_filterDescription'));
    jobSelect = element(by.css('select#field_job'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setFilterDescriptionInput = function(filterDescription) {
        this.filterDescriptionInput.sendKeys(filterDescription);
    };

    getFilterDescriptionInput = function() {
        return this.filterDescriptionInput.getAttribute('value');
    };

    jobSelectLastOption = function() {
        this.jobSelect.all(by.tagName('option')).last().click();
    };

    jobSelectOption = function(option) {
        this.jobSelect.sendKeys(option);
    };

    getJobSelect = function() {
        return this.jobSelect;
    };

    getJobSelectedOption = function() {
        return this.jobSelect.element(by.css('option:checked')).getText();
    };

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
