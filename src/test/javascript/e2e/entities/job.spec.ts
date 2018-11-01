import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Job e2e test', () => {

    let navBarPage: NavBarPage;
    let jobDialogPage: JobDialogPage;
    let jobComponentsPage: JobComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Jobs', () => {
        navBarPage.goToEntity('job');
        jobComponentsPage = new JobComponentsPage();
        expect(jobComponentsPage.getTitle())
            .toMatch(/gradzcircleApp.job.home.title/);

    });

    it('should load create Job dialog', () => {
        jobComponentsPage.clickOnCreateButton();
        jobDialogPage = new JobDialogPage();
        expect(jobDialogPage.getModalTitle())
            .toMatch(/gradzcircleApp.job.home.createOrEditLabel/);
        jobDialogPage.close();
    });

    it('should create and save Jobs', () => {
        jobComponentsPage.clickOnCreateButton();
        jobDialogPage.setJobTitleInput('jobTitle');
        expect(jobDialogPage.getJobTitleInput()).toMatch('jobTitle');
        jobDialogPage.setJobDescriptionInput('jobDescription');
        expect(jobDialogPage.getJobDescriptionInput()).toMatch('jobDescription');
        jobDialogPage.setNoOfApplicantsInput('5');
        expect(jobDialogPage.getNoOfApplicantsInput()).toMatch('5');
        jobDialogPage.setSalaryInput('5');
        expect(jobDialogPage.getSalaryInput()).toMatch('5');
        jobDialogPage.setJobStatusInput('5');
        expect(jobDialogPage.getJobStatusInput()).toMatch('5');
        jobDialogPage.setCreateDateInput(12310020012301);
        expect(jobDialogPage.getCreateDateInput()).toMatch('2001-12-31T02:30');
        jobDialogPage.setOriginalJobCostInput('5');
        expect(jobDialogPage.getOriginalJobCostInput()).toMatch('5');
        jobDialogPage.setJobCostInput('5');
        expect(jobDialogPage.getJobCostInput()).toMatch('5');
        jobDialogPage.setAmountPaidInput('5');
        expect(jobDialogPage.getAmountPaidInput()).toMatch('5');
        jobDialogPage.setTotalAmountPaidInput('5');
        expect(jobDialogPage.getTotalAmountPaidInput()).toMatch('5');
        jobDialogPage.setNoOfApplicantsBoughtInput('5');
        expect(jobDialogPage.getNoOfApplicantsBoughtInput()).toMatch('5');
        jobDialogPage.setRemovedFilterAmountInput('5');
        expect(jobDialogPage.getRemovedFilterAmountInput()).toMatch('5');
        jobDialogPage.setAdditionalFilterAmountInput('5');
        expect(jobDialogPage.getAdditionalFilterAmountInput()).toMatch('5');
        jobDialogPage.setAdminChargeInput('5');
        expect(jobDialogPage.getAdminChargeInput()).toMatch('5');
        jobDialogPage.setAdminChargeRateInput('5');
        expect(jobDialogPage.getAdminChargeRateInput()).toMatch('5');
        jobDialogPage.setUpfrontDiscountRateInput('5');
        expect(jobDialogPage.getUpfrontDiscountRateInput()).toMatch('5');
        jobDialogPage.setUpfrontDiscountAmountInput('5');
        expect(jobDialogPage.getUpfrontDiscountAmountInput()).toMatch('5');
        jobDialogPage.setEscrowAmountUsedInput('5');
        expect(jobDialogPage.getEscrowAmountUsedInput()).toMatch('5');
        jobDialogPage.setEscrowAmountAddedInput('5');
        expect(jobDialogPage.getEscrowAmountAddedInput()).toMatch('5');
        jobDialogPage.paymentTypeSelectLastOption();
        jobDialogPage.getHasBeenEditedInput().isSelected().then((selected) => {
            if (selected) {
                jobDialogPage.getHasBeenEditedInput().click();
                expect(jobDialogPage.getHasBeenEditedInput().isSelected()).toBeFalsy();
            } else {
                jobDialogPage.getHasBeenEditedInput().click();
                expect(jobDialogPage.getHasBeenEditedInput().isSelected()).toBeTruthy();
            }
        });
        jobDialogPage.getEverActiveInput().isSelected().then((selected) => {
            if (selected) {
                jobDialogPage.getEverActiveInput().click();
                expect(jobDialogPage.getEverActiveInput().isSelected()).toBeFalsy();
            } else {
                jobDialogPage.getEverActiveInput().click();
                expect(jobDialogPage.getEverActiveInput().isSelected()).toBeTruthy();
            }
        });
        jobDialogPage.getCanEditInput().isSelected().then((selected) => {
            if (selected) {
                jobDialogPage.getCanEditInput().click();
                expect(jobDialogPage.getCanEditInput().isSelected()).toBeFalsy();
            } else {
                jobDialogPage.getCanEditInput().click();
                expect(jobDialogPage.getCanEditInput().isSelected()).toBeTruthy();
            }
        });
        jobDialogPage.setUpdateDateInput(12310020012301);
        expect(jobDialogPage.getUpdateDateInput()).toMatch('2001-12-31T02:30');
        jobDialogPage.setCreatedByInput('5');
        expect(jobDialogPage.getCreatedByInput()).toMatch('5');
        jobDialogPage.setUpdatedByInput('5');
        expect(jobDialogPage.getUpdatedByInput()).toMatch('5');
        jobDialogPage.jobTypeSelectLastOption();
        jobDialogPage.employmentTypeSelectLastOption();
        jobDialogPage.corporateSelectLastOption();
        jobDialogPage.save();
        expect(jobDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class JobComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-job div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class JobDialogPage {
    modalTitle = element(by.css('h4#myJobLabel'));
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
    jobTypeSelect = element(by.css('select#field_jobType'));
    employmentTypeSelect = element(by.css('select#field_employmentType'));
    corporateSelect = element(by.css('select#field_corporate'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setJobTitleInput = function(jobTitle) {
        this.jobTitleInput.sendKeys(jobTitle);
    };

    getJobTitleInput = function() {
        return this.jobTitleInput.getAttribute('value');
    };

    setJobDescriptionInput = function(jobDescription) {
        this.jobDescriptionInput.sendKeys(jobDescription);
    };

    getJobDescriptionInput = function() {
        return this.jobDescriptionInput.getAttribute('value');
    };

    setNoOfApplicantsInput = function(noOfApplicants) {
        this.noOfApplicantsInput.sendKeys(noOfApplicants);
    };

    getNoOfApplicantsInput = function() {
        return this.noOfApplicantsInput.getAttribute('value');
    };

    setSalaryInput = function(salary) {
        this.salaryInput.sendKeys(salary);
    };

    getSalaryInput = function() {
        return this.salaryInput.getAttribute('value');
    };

    setJobStatusInput = function(jobStatus) {
        this.jobStatusInput.sendKeys(jobStatus);
    };

    getJobStatusInput = function() {
        return this.jobStatusInput.getAttribute('value');
    };

    setCreateDateInput = function(createDate) {
        this.createDateInput.sendKeys(createDate);
    };

    getCreateDateInput = function() {
        return this.createDateInput.getAttribute('value');
    };

    setOriginalJobCostInput = function(originalJobCost) {
        this.originalJobCostInput.sendKeys(originalJobCost);
    };

    getOriginalJobCostInput = function() {
        return this.originalJobCostInput.getAttribute('value');
    };

    setJobCostInput = function(jobCost) {
        this.jobCostInput.sendKeys(jobCost);
    };

    getJobCostInput = function() {
        return this.jobCostInput.getAttribute('value');
    };

    setAmountPaidInput = function(amountPaid) {
        this.amountPaidInput.sendKeys(amountPaid);
    };

    getAmountPaidInput = function() {
        return this.amountPaidInput.getAttribute('value');
    };

    setTotalAmountPaidInput = function(totalAmountPaid) {
        this.totalAmountPaidInput.sendKeys(totalAmountPaid);
    };

    getTotalAmountPaidInput = function() {
        return this.totalAmountPaidInput.getAttribute('value');
    };

    setNoOfApplicantsBoughtInput = function(noOfApplicantsBought) {
        this.noOfApplicantsBoughtInput.sendKeys(noOfApplicantsBought);
    };

    getNoOfApplicantsBoughtInput = function() {
        return this.noOfApplicantsBoughtInput.getAttribute('value');
    };

    setRemovedFilterAmountInput = function(removedFilterAmount) {
        this.removedFilterAmountInput.sendKeys(removedFilterAmount);
    };

    getRemovedFilterAmountInput = function() {
        return this.removedFilterAmountInput.getAttribute('value');
    };

    setAdditionalFilterAmountInput = function(additionalFilterAmount) {
        this.additionalFilterAmountInput.sendKeys(additionalFilterAmount);
    };

    getAdditionalFilterAmountInput = function() {
        return this.additionalFilterAmountInput.getAttribute('value');
    };

    setAdminChargeInput = function(adminCharge) {
        this.adminChargeInput.sendKeys(adminCharge);
    };

    getAdminChargeInput = function() {
        return this.adminChargeInput.getAttribute('value');
    };

    setAdminChargeRateInput = function(adminChargeRate) {
        this.adminChargeRateInput.sendKeys(adminChargeRate);
    };

    getAdminChargeRateInput = function() {
        return this.adminChargeRateInput.getAttribute('value');
    };

    setUpfrontDiscountRateInput = function(upfrontDiscountRate) {
        this.upfrontDiscountRateInput.sendKeys(upfrontDiscountRate);
    };

    getUpfrontDiscountRateInput = function() {
        return this.upfrontDiscountRateInput.getAttribute('value');
    };

    setUpfrontDiscountAmountInput = function(upfrontDiscountAmount) {
        this.upfrontDiscountAmountInput.sendKeys(upfrontDiscountAmount);
    };

    getUpfrontDiscountAmountInput = function() {
        return this.upfrontDiscountAmountInput.getAttribute('value');
    };

    setEscrowAmountUsedInput = function(escrowAmountUsed) {
        this.escrowAmountUsedInput.sendKeys(escrowAmountUsed);
    };

    getEscrowAmountUsedInput = function() {
        return this.escrowAmountUsedInput.getAttribute('value');
    };

    setEscrowAmountAddedInput = function(escrowAmountAdded) {
        this.escrowAmountAddedInput.sendKeys(escrowAmountAdded);
    };

    getEscrowAmountAddedInput = function() {
        return this.escrowAmountAddedInput.getAttribute('value');
    };

    setPaymentTypeSelect = function(paymentType) {
        this.paymentTypeSelect.sendKeys(paymentType);
    };

    getPaymentTypeSelect = function() {
        return this.paymentTypeSelect.element(by.css('option:checked')).getText();
    };

    paymentTypeSelectLastOption = function() {
        this.paymentTypeSelect.all(by.tagName('option')).last().click();
    };
    getHasBeenEditedInput = function() {
        return this.hasBeenEditedInput;
    };
    getEverActiveInput = function() {
        return this.everActiveInput;
    };
    getCanEditInput = function() {
        return this.canEditInput;
    };
    setUpdateDateInput = function(updateDate) {
        this.updateDateInput.sendKeys(updateDate);
    };

    getUpdateDateInput = function() {
        return this.updateDateInput.getAttribute('value');
    };

    setCreatedByInput = function(createdBy) {
        this.createdByInput.sendKeys(createdBy);
    };

    getCreatedByInput = function() {
        return this.createdByInput.getAttribute('value');
    };

    setUpdatedByInput = function(updatedBy) {
        this.updatedByInput.sendKeys(updatedBy);
    };

    getUpdatedByInput = function() {
        return this.updatedByInput.getAttribute('value');
    };

    jobTypeSelectLastOption = function() {
        this.jobTypeSelect.all(by.tagName('option')).last().click();
    };

    jobTypeSelectOption = function(option) {
        this.jobTypeSelect.sendKeys(option);
    };

    getJobTypeSelect = function() {
        return this.jobTypeSelect;
    };

    getJobTypeSelectedOption = function() {
        return this.jobTypeSelect.element(by.css('option:checked')).getText();
    };

    employmentTypeSelectLastOption = function() {
        this.employmentTypeSelect.all(by.tagName('option')).last().click();
    };

    employmentTypeSelectOption = function(option) {
        this.employmentTypeSelect.sendKeys(option);
    };

    getEmploymentTypeSelect = function() {
        return this.employmentTypeSelect;
    };

    getEmploymentTypeSelectedOption = function() {
        return this.employmentTypeSelect.element(by.css('option:checked')).getText();
    };

    corporateSelectLastOption = function() {
        this.corporateSelect.all(by.tagName('option')).last().click();
    };

    corporateSelectOption = function(option) {
        this.corporateSelect.sendKeys(option);
    };

    getCorporateSelect = function() {
        return this.corporateSelect;
    };

    getCorporateSelectedOption = function() {
        return this.corporateSelect.element(by.css('option:checked')).getText();
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
