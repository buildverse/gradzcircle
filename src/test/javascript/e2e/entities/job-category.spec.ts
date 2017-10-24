import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('JobCategory e2e test', () => {

    let navBarPage: NavBarPage;
    let jobCategoryDialogPage: JobCategoryDialogPage;
    let jobCategoryComponentsPage: JobCategoryComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load JobCategories', () => {
        navBarPage.goToEntity('job-category');
        jobCategoryComponentsPage = new JobCategoryComponentsPage();
        expect(jobCategoryComponentsPage.getTitle()).toMatch(/gradzcircleApp.jobCategory.home.title/);

    });

    it('should load create JobCategory dialog', () => {
        jobCategoryComponentsPage.clickOnCreateButton();
        jobCategoryDialogPage = new JobCategoryDialogPage();
        expect(jobCategoryDialogPage.getModalTitle()).toMatch(/gradzcircleApp.jobCategory.home.createOrEditLabel/);
        jobCategoryDialogPage.close();
    });

    it('should create and save JobCategories', () => {
        jobCategoryComponentsPage.clickOnCreateButton();
        jobCategoryDialogPage.setJobCategoryInput('jobCategory');
        expect(jobCategoryDialogPage.getJobCategoryInput()).toMatch('jobCategory');
        jobCategoryDialogPage.save();
        expect(jobCategoryDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class JobCategoryComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-job-category div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class JobCategoryDialogPage {
    modalTitle = element(by.css('h4#myJobCategoryLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    jobCategoryInput = element(by.css('input#field_jobCategory'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setJobCategoryInput = function (jobCategory) {
        this.jobCategoryInput.sendKeys(jobCategory);
    }

    getJobCategoryInput = function () {
        return this.jobCategoryInput.getAttribute('value');
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
