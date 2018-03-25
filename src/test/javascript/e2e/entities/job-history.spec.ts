import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('JobHistory e2e test', () => {

    let navBarPage: NavBarPage;
    let jobHistoryDialogPage: JobHistoryDialogPage;
    let jobHistoryComponentsPage: JobHistoryComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load JobHistories', () => {
        navBarPage.goToEntity('job-history');
        jobHistoryComponentsPage = new JobHistoryComponentsPage();
        expect(jobHistoryComponentsPage.getTitle()).toMatch(/gradzcircleApp.jobHistory.home.title/);

    });

    it('should load create JobHistory dialog', () => {
        jobHistoryComponentsPage.clickOnCreateButton();
        jobHistoryDialogPage = new JobHistoryDialogPage();
        expect(jobHistoryDialogPage.getModalTitle()).toMatch(/gradzcircleApp.jobHistory.home.createOrEditLabel/);
        jobHistoryDialogPage.close();
    });

    it('should create and save JobHistories', () => {
        jobHistoryComponentsPage.clickOnCreateButton();
        jobHistoryDialogPage.setJobTitleInput('jobTitle');
        expect(jobHistoryDialogPage.getJobTitleInput()).toMatch('jobTitle');
        jobHistoryDialogPage.setJobDescriptionInput('jobDescription');
        expect(jobHistoryDialogPage.getJobDescriptionInput()).toMatch('jobDescription');
        jobHistoryDialogPage.setNoOfApplicantsInput('5');
        expect(jobHistoryDialogPage.getNoOfApplicantsInput()).toMatch('5');
        jobHistoryDialogPage.setSalaryInput('5');
        expect(jobHistoryDialogPage.getSalaryInput()).toMatch('5');
        jobHistoryDialogPage.setJobStatusInput('5');
        expect(jobHistoryDialogPage.getJobStatusInput()).toMatch('5');
        jobHistoryDialogPage.setCreateDateInput(12310020012301);
        expect(jobHistoryDialogPage.getCreateDateInput()).toMatch('2001-12-31T02:30');
        jobHistoryDialogPage.setJobCostInput('5');
        expect(jobHistoryDialogPage.getJobCostInput()).toMatch('5');
        jobHistoryDialogPage.setAmountPaidInput('5');
        expect(jobHistoryDialogPage.getAmountPaidInput()).toMatch('5');
        jobHistoryDialogPage.setEscrowAmountUsedInput('5');
        expect(jobHistoryDialogPage.getEscrowAmountUsedInput()).toMatch('5');
        jobHistoryDialogPage.setAdditionalAmountChargeInput('5');
        expect(jobHistoryDialogPage.getAdditionalAmountChargeInput()).toMatch('5');
        jobHistoryDialogPage.paymentTypeSelectLastOption();
        jobHistoryDialogPage.getHasBeenEditedInput().isSelected().then(function (selected) {
            if (selected) {
                jobHistoryDialogPage.getHasBeenEditedInput().click();
                expect(jobHistoryDialogPage.getHasBeenEditedInput().isSelected()).toBeFalsy();
            } else {
                jobHistoryDialogPage.getHasBeenEditedInput().click();
                expect(jobHistoryDialogPage.getHasBeenEditedInput().isSelected()).toBeTruthy();
            }
        });
        jobHistoryDialogPage.getEverActiveInput().isSelected().then(function (selected) {
            if (selected) {
                jobHistoryDialogPage.getEverActiveInput().click();
                expect(jobHistoryDialogPage.getEverActiveInput().isSelected()).toBeFalsy();
            } else {
                jobHistoryDialogPage.getEverActiveInput().click();
                expect(jobHistoryDialogPage.getEverActiveInput().isSelected()).toBeTruthy();
            }
        });
        jobHistoryDialogPage.getCanEditInput().isSelected().then(function (selected) {
            if (selected) {
                jobHistoryDialogPage.getCanEditInput().click();
                expect(jobHistoryDialogPage.getCanEditInput().isSelected()).toBeFalsy();
            } else {
                jobHistoryDialogPage.getCanEditInput().click();
                expect(jobHistoryDialogPage.getCanEditInput().isSelected()).toBeTruthy();
            }
        });
        jobHistoryDialogPage.setUpdateDateInput(12310020012301);
        expect(jobHistoryDialogPage.getUpdateDateInput()).toMatch('2001-12-31T02:30');
        jobHistoryDialogPage.setCreatedByInput('5');
        expect(jobHistoryDialogPage.getCreatedByInput()).toMatch('5');
        jobHistoryDialogPage.setUpdatedByInput('5');
        expect(jobHistoryDialogPage.getUpdatedByInput()).toMatch('5');
        jobHistoryDialogPage.jobSelectLastOption();
        jobHistoryDialogPage.save();
        expect(jobHistoryDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class JobHistoryComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-job-history div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class JobHistoryDialogPage {
    modalTitle = element(by.css('h4#myJobHistoryLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    jobTitleInput = element(by.css('input#field_jobTitle'));
    jobDescriptionInput = element(by.css('input#field_jobDescription'));
    noOfApplicantsInput = element(by.css('input#field_noOfApplicants'));
    salaryInput = element(by.css('input#field_salary'));
    jobStatusInput = element(by.css('input#field_jobStatus'));
    createDateInput = element(by.css('input#field_createDate'));
    jobCostInput = element(by.css('input#field_jobCost'));
    amountPaidInput = element(by.css('input#field_amountPaid'));
    escrowAmountUsedInput = element(by.css('input#field_escrowAmountUsed'));
    additionalAmountChargeInput = element(by.css('input#field_additionalAmountCharge'));
    paymentTypeSelect = element(by.css('select#field_paymentType'));
    hasBeenEditedInput = element(by.css('input#field_hasBeenEdited'));
    everActiveInput = element(by.css('input#field_everActive'));
    canEditInput = element(by.css('input#field_canEdit'));
    updateDateInput = element(by.css('input#field_updateDate'));
    createdByInput = element(by.css('input#field_createdBy'));
    updatedByInput = element(by.css('input#field_updatedBy'));
    jobSelect = element(by.css('select#field_job'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setJobTitleInput = function (jobTitle) {
        this.jobTitleInput.sendKeys(jobTitle);
    }

    getJobTitleInput = function () {
        return this.jobTitleInput.getAttribute('value');
    }

    setJobDescriptionInput = function (jobDescription) {
        this.jobDescriptionInput.sendKeys(jobDescription);
    }

    getJobDescriptionInput = function () {
        return this.jobDescriptionInput.getAttribute('value');
    }

    setNoOfApplicantsInput = function (noOfApplicants) {
        this.noOfApplicantsInput.sendKeys(noOfApplicants);
    }

    getNoOfApplicantsInput = function () {
        return this.noOfApplicantsInput.getAttribute('value');
    }

    setSalaryInput = function (salary) {
        this.salaryInput.sendKeys(salary);
    }

    getSalaryInput = function () {
        return this.salaryInput.getAttribute('value');
    }

    setJobStatusInput = function (jobStatus) {
        this.jobStatusInput.sendKeys(jobStatus);
    }

    getJobStatusInput = function () {
        return this.jobStatusInput.getAttribute('value');
    }

    setCreateDateInput = function (createDate) {
        this.createDateInput.sendKeys(createDate);
    }

    getCreateDateInput = function () {
        return this.createDateInput.getAttribute('value');
    }

    setJobCostInput = function (jobCost) {
        this.jobCostInput.sendKeys(jobCost);
    }

    getJobCostInput = function () {
        return this.jobCostInput.getAttribute('value');
    }

    setAmountPaidInput = function (amountPaid) {
        this.amountPaidInput.sendKeys(amountPaid);
    }

    getAmountPaidInput = function () {
        return this.amountPaidInput.getAttribute('value');
    }

    setEscrowAmountUsedInput = function (escrowAmountUsed) {
        this.escrowAmountUsedInput.sendKeys(escrowAmountUsed);
    }

    getEscrowAmountUsedInput = function () {
        return this.escrowAmountUsedInput.getAttribute('value');
    }

    setAdditionalAmountChargeInput = function (additionalAmountCharge) {
        this.additionalAmountChargeInput.sendKeys(additionalAmountCharge);
    }

    getAdditionalAmountChargeInput = function () {
        return this.additionalAmountChargeInput.getAttribute('value');
    }

    setPaymentTypeSelect = function (paymentType) {
        this.paymentTypeSelect.sendKeys(paymentType);
    }

    getPaymentTypeSelect = function () {
        return this.paymentTypeSelect.element(by.css('option:checked')).getText();
    }

    paymentTypeSelectLastOption = function () {
        this.paymentTypeSelect.all(by.tagName('option')).last().click();
    }
    getHasBeenEditedInput = function () {
        return this.hasBeenEditedInput;
    }
    getEverActiveInput = function () {
        return this.everActiveInput;
    }
    getCanEditInput = function () {
        return this.canEditInput;
    }
    setUpdateDateInput = function (updateDate) {
        this.updateDateInput.sendKeys(updateDate);
    }

    getUpdateDateInput = function () {
        return this.updateDateInput.getAttribute('value');
    }

    setCreatedByInput = function (createdBy) {
        this.createdByInput.sendKeys(createdBy);
    }

    getCreatedByInput = function () {
        return this.createdByInput.getAttribute('value');
    }

    setUpdatedByInput = function (updatedBy) {
        this.updatedByInput.sendKeys(updatedBy);
    }

    getUpdatedByInput = function () {
        return this.updatedByInput.getAttribute('value');
    }

    jobSelectLastOption = function () {
        this.jobSelect.all(by.tagName('option')).last().click();
    }

    jobSelectOption = function (option) {
        this.jobSelect.sendKeys(option);
    }

    getJobSelect = function () {
        return this.jobSelect;
    }

    getJobSelectedOption = function () {
        return this.jobSelect.element(by.css('option:checked')).getText();
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
