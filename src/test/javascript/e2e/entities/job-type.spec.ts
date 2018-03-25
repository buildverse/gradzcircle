import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('JobType e2e test', () => {

    let navBarPage: NavBarPage;
    let jobTypeDialogPage: JobTypeDialogPage;
    let jobTypeComponentsPage: JobTypeComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load JobTypes', () => {
        navBarPage.goToEntity('job-type');
        jobTypeComponentsPage = new JobTypeComponentsPage();
        expect(jobTypeComponentsPage.getTitle()).toMatch(/gradzcircleApp.jobType.home.title/);

    });

    it('should load create JobType dialog', () => {
        jobTypeComponentsPage.clickOnCreateButton();
        jobTypeDialogPage = new JobTypeDialogPage();
        expect(jobTypeDialogPage.getModalTitle()).toMatch(/gradzcircleApp.jobType.home.createOrEditLabel/);
        jobTypeDialogPage.close();
    });

    it('should create and save JobTypes', () => {
        jobTypeComponentsPage.clickOnCreateButton();
        jobTypeDialogPage.setJobTypeInput('jobType');
        expect(jobTypeDialogPage.getJobTypeInput()).toMatch('jobType');
        jobTypeDialogPage.setJobTypeCostInput('5');
        expect(jobTypeDialogPage.getJobTypeCostInput()).toMatch('5');
        jobTypeDialogPage.save();
        expect(jobTypeDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class JobTypeComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-job-type div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class JobTypeDialogPage {
    modalTitle = element(by.css('h4#myJobTypeLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    jobTypeInput = element(by.css('input#field_jobType'));
    jobTypeCostInput = element(by.css('input#field_jobTypeCost'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setJobTypeInput = function (jobType) {
        this.jobTypeInput.sendKeys(jobType);
    }

    getJobTypeInput = function () {
        return this.jobTypeInput.getAttribute('value');
    }

    setJobTypeCostInput = function (jobTypeCost) {
        this.jobTypeCostInput.sendKeys(jobTypeCost);
    }

    getJobTypeCostInput = function () {
        return this.jobTypeCostInput.getAttribute('value');
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
