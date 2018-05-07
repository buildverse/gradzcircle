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
        jobHistoryDialogPage.setOriginalJobCostInput('5');
        expect(jobHistoryDialogPage.getOriginalJobCostInput()).toMatch('5');
        jobHistoryDialogPage.setJobCostInput('5');
        expect(jobHistoryDialogPage.getJobCostInput()).toMatch('5');
        jobHistoryDialogPage.setAmountPaidInput('5');
        expect(jobHistoryDialogPage.getAmountPaidInput()).toMatch('5');
        jobHistoryDialogPage.setTotalAmountPaidInput('5');
        expect(jobHistoryDialogPage.getTotalAmountPaidInput()).toMatch('5');
        jobHistoryDialogPage.setNoOfApplicantsBoughtInput('5');
        expect(jobHistoryDialogPage.getNoOfApplicantsBoughtInput()).toMatch('5');
        jobHistoryDialogPage.setRemovedFilterAmountInput('5');
        expect(jobHistoryDialogPage.getRemovedFilterAmountInput()).toMatch('5');
        jobHistoryDialogPage.setAdditionalFilterAmountInput('5');
        expect(jobHistoryDialogPage.getAdditionalFilterAmountInput()).toMatch('5');
        jobHistoryDialogPage.setAdminChargeInput('5');
        expect(jobHistoryDialogPage.getAdminChargeInput()).toMatch('5');
        jobHistoryDialogPage.setAdminChargeRateInput('5');
        expect(jobHistoryDialogPage.getAdminChargeRateInput()).toMatch('5');
        jobHistoryDialogPage.setUpfrontDiscountRateInput('5');
        expect(jobHistoryDialogPage.getUpfrontDiscountRateInput()).toMatch('5');
        jobHistoryDialogPage.setUpfrontDiscountAmountInput('5');
        expect(jobHistoryDialogPage.getUpfrontDiscountAmountInput()).toMatch('5');
        jobHistoryDialogPage.setEscrowAmountUsedInput('5');
        expect(jobHistoryDialogPage.getEscrowAmountUsedInput()).toMatch('5');
        jobHistoryDialogPage.setEscrowAmountAddedInput('5');
        expect(jobHistoryDialogPage.getEscrowAmountAddedInput()).toMatch('5');
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
    originalJobCostInput = element(by.css('input#field_originalJobCost'));
    jobCostInput = element(by.css('input#field_jobCost'));
    amountPaidInput = element(by.css('input#field_amountPaid'));
    totalAmountPaidInput = element(by.css('input#field_totalAmountPaid'));
    noOfApplicantsBoughtInput = element(by.css('input#field_noOfApplicantsBought'));
    removedFilterAmountInput = element(by.css('input#field_removedFilterAmount'));
    additionalFilterAmountInput = element(by.css('input#field_additionalFilterAmount'));
    adminChargeInput = element(by.css('input#field_adminCharge'));
    adminChargeRateInput = element(by.css('input#field_adminChargeRate'));
    upfrontDiscountRateInput = element(by.css('input#field_upfrontDiscountRate'));
    upfrontDiscountAmountInput = element(by.css('input#field_upfrontDiscountAmount'));
    escrowAmountUsedInput = element(by.css('input#field_escrowAmountUsed'));
    escrowAmountAddedInput = element(by.css('input#field_escrowAmountAdded'));
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

    setOriginalJobCostInput = function (originalJobCost) {
        this.originalJobCostInput.sendKeys(originalJobCost);
    }

    getOriginalJobCostInput = function () {
        return this.originalJobCostInput.getAttribute('value');
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

    setTotalAmountPaidInput = function (totalAmountPaid) {
        this.totalAmountPaidInput.sendKeys(totalAmountPaid);
    }

    getTotalAmountPaidInput = function () {
        return this.totalAmountPaidInput.getAttribute('value');
    }

    setNoOfApplicantsBoughtInput = function (noOfApplicantsBought) {
        this.noOfApplicantsBoughtInput.sendKeys(noOfApplicantsBought);
    }

    getNoOfApplicantsBoughtInput = function () {
        return this.noOfApplicantsBoughtInput.getAttribute('value');
    }

    setRemovedFilterAmountInput = function (removedFilterAmount) {
        this.removedFilterAmountInput.sendKeys(removedFilterAmount);
    }

    getRemovedFilterAmountInput = function () {
        return this.removedFilterAmountInput.getAttribute('value');
    }

    setAdditionalFilterAmountInput = function (additionalFilterAmount) {
        this.additionalFilterAmountInput.sendKeys(additionalFilterAmount);
    }

    getAdditionalFilterAmountInput = function () {
        return this.additionalFilterAmountInput.getAttribute('value');
    }

    setAdminChargeInput = function (adminCharge) {
        this.adminChargeInput.sendKeys(adminCharge);
    }

    getAdminChargeInput = function () {
        return this.adminChargeInput.getAttribute('value');
    }

    setAdminChargeRateInput = function (adminChargeRate) {
        this.adminChargeRateInput.sendKeys(adminChargeRate);
    }

    getAdminChargeRateInput = function () {
        return this.adminChargeRateInput.getAttribute('value');
    }

    setUpfrontDiscountRateInput = function (upfrontDiscountRate) {
        this.upfrontDiscountRateInput.sendKeys(upfrontDiscountRate);
    }

    getUpfrontDiscountRateInput = function () {
        return this.upfrontDiscountRateInput.getAttribute('value');
    }

    setUpfrontDiscountAmountInput = function (upfrontDiscountAmount) {
        this.upfrontDiscountAmountInput.sendKeys(upfrontDiscountAmount);
    }

    getUpfrontDiscountAmountInput = function () {
        return this.upfrontDiscountAmountInput.getAttribute('value');
    }

    setEscrowAmountUsedInput = function (escrowAmountUsed) {
        this.escrowAmountUsedInput.sendKeys(escrowAmountUsed);
    }

    getEscrowAmountUsedInput = function () {
        return this.escrowAmountUsedInput.getAttribute('value');
    }

    setEscrowAmountAddedInput = function (escrowAmountAdded) {
        this.escrowAmountAddedInput.sendKeys(escrowAmountAdded);
    }

    getEscrowAmountAddedInput = function () {
        return this.escrowAmountAddedInput.getAttribute('value');
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
