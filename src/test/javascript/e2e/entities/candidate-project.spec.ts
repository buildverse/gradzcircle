import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('CandidateProject e2e test', () => {
    let navBarPage: NavBarPage;
    let candidateProjectDialogPage: CandidateProjectDialogPage;
    let candidateProjectComponentsPage: CandidateProjectComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load CandidateProjects', () => {
        navBarPage.goToEntity('candidate-project');
        candidateProjectComponentsPage = new CandidateProjectComponentsPage();
        expect(candidateProjectComponentsPage.getTitle()).toMatch(/gradzcircleApp.candidateProject.home.title/);
    });

    it('should load create CandidateProject dialog', () => {
        candidateProjectComponentsPage.clickOnCreateButton();
        candidateProjectDialogPage = new CandidateProjectDialogPage();
        expect(candidateProjectDialogPage.getModalTitle()).toMatch(/gradzcircleApp.candidateProject.home.createOrEditLabel/);
        candidateProjectDialogPage.close();
    });

    it('should create and save CandidateProjects', () => {
        candidateProjectComponentsPage.clickOnCreateButton();
        candidateProjectDialogPage.setProjectTitleInput('projectTitle');
        expect(candidateProjectDialogPage.getProjectTitleInput()).toMatch('projectTitle');
        candidateProjectDialogPage.setProjectStartDateInput('2000-12-31');
        expect(candidateProjectDialogPage.getProjectStartDateInput()).toMatch('2000-12-31');
        candidateProjectDialogPage.setProjectEndDateInput('2000-12-31');
        expect(candidateProjectDialogPage.getProjectEndDateInput()).toMatch('2000-12-31');
        candidateProjectDialogPage.setProjectDescriptionInput('projectDescription');
        expect(candidateProjectDialogPage.getProjectDescriptionInput()).toMatch('projectDescription');
        candidateProjectDialogPage.setProjectDurationInput('5');
        expect(candidateProjectDialogPage.getProjectDurationInput()).toMatch('5');
        candidateProjectDialogPage.setContributionInProjectInput('contributionInProject');
        expect(candidateProjectDialogPage.getContributionInProjectInput()).toMatch('contributionInProject');
        candidateProjectDialogPage
            .getIsCurrentProjectInput()
            .isSelected()
            .then(selected => {
                if (selected) {
                    candidateProjectDialogPage.getIsCurrentProjectInput().click();
                    expect(candidateProjectDialogPage.getIsCurrentProjectInput().isSelected()).toBeFalsy();
                } else {
                    candidateProjectDialogPage.getIsCurrentProjectInput().click();
                    expect(candidateProjectDialogPage.getIsCurrentProjectInput().isSelected()).toBeTruthy();
                }
            });
        candidateProjectDialogPage.projectTypeSelectLastOption();
        candidateProjectDialogPage.educationSelectLastOption();
        candidateProjectDialogPage.employmentSelectLastOption();
        candidateProjectDialogPage.save();
        expect(candidateProjectDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CandidateProjectComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-candidate-project div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CandidateProjectDialogPage {
    modalTitle = element(by.css('h4#myCandidateProjectLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    projectTitleInput = element(by.css('input#field_projectTitle'));
    projectStartDateInput = element(by.css('input#field_projectStartDate'));
    projectEndDateInput = element(by.css('input#field_projectEndDate'));
    projectDescriptionInput = element(by.css('input#field_projectDescription'));
    projectDurationInput = element(by.css('input#field_projectDuration'));
    contributionInProjectInput = element(by.css('input#field_contributionInProject'));
    isCurrentProjectInput = element(by.css('input#field_isCurrentProject'));
    projectTypeSelect = element(by.css('select#field_projectType'));
    educationSelect = element(by.css('select#field_education'));
    employmentSelect = element(by.css('select#field_employment'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setProjectTitleInput = function(projectTitle) {
        this.projectTitleInput.sendKeys(projectTitle);
    };

    getProjectTitleInput = function() {
        return this.projectTitleInput.getAttribute('value');
    };

    setProjectStartDateInput = function(projectStartDate) {
        this.projectStartDateInput.sendKeys(projectStartDate);
    };

    getProjectStartDateInput = function() {
        return this.projectStartDateInput.getAttribute('value');
    };

    setProjectEndDateInput = function(projectEndDate) {
        this.projectEndDateInput.sendKeys(projectEndDate);
    };

    getProjectEndDateInput = function() {
        return this.projectEndDateInput.getAttribute('value');
    };

    setProjectDescriptionInput = function(projectDescription) {
        this.projectDescriptionInput.sendKeys(projectDescription);
    };

    getProjectDescriptionInput = function() {
        return this.projectDescriptionInput.getAttribute('value');
    };

    setProjectDurationInput = function(projectDuration) {
        this.projectDurationInput.sendKeys(projectDuration);
    };

    getProjectDurationInput = function() {
        return this.projectDurationInput.getAttribute('value');
    };

    setContributionInProjectInput = function(contributionInProject) {
        this.contributionInProjectInput.sendKeys(contributionInProject);
    };

    getContributionInProjectInput = function() {
        return this.contributionInProjectInput.getAttribute('value');
    };

    getIsCurrentProjectInput = function() {
        return this.isCurrentProjectInput;
    };
    setProjectTypeSelect = function(projectType) {
        this.projectTypeSelect.sendKeys(projectType);
    };

    getProjectTypeSelect = function() {
        return this.projectTypeSelect.element(by.css('option:checked')).getText();
    };

    projectTypeSelectLastOption = function() {
        this.projectTypeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    };
    educationSelectLastOption = function() {
        this.educationSelect
            .all(by.tagName('option'))
            .last()
            .click();
    };

    educationSelectOption = function(option) {
        this.educationSelect.sendKeys(option);
    };

    getEducationSelect = function() {
        return this.educationSelect;
    };

    getEducationSelectedOption = function() {
        return this.educationSelect.element(by.css('option:checked')).getText();
    };

    employmentSelectLastOption = function() {
        this.employmentSelect
            .all(by.tagName('option'))
            .last()
            .click();
    };

    employmentSelectOption = function(option) {
        this.employmentSelect.sendKeys(option);
    };

    getEmploymentSelect = function() {
        return this.employmentSelect;
    };

    getEmploymentSelectedOption = function() {
        return this.employmentSelect.element(by.css('option:checked')).getText();
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
