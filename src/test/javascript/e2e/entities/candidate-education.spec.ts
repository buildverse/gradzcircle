import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('CandidateEducation e2e test', () => {

    let navBarPage: NavBarPage;
    let candidateEducationDialogPage: CandidateEducationDialogPage;
    let candidateEducationComponentsPage: CandidateEducationComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load CandidateEducations', () => {
        navBarPage.goToEntity('candidate-education');
        candidateEducationComponentsPage = new CandidateEducationComponentsPage();
        expect(candidateEducationComponentsPage.getTitle())
            .toMatch(/gradzcircleApp.candidateEducation.home.title/);

    });

    it('should load create CandidateEducation dialog', () => {
        candidateEducationComponentsPage.clickOnCreateButton();
        candidateEducationDialogPage = new CandidateEducationDialogPage();
        expect(candidateEducationDialogPage.getModalTitle())
            .toMatch(/gradzcircleApp.candidateEducation.home.createOrEditLabel/);
        candidateEducationDialogPage.close();
    });

    it('should create and save CandidateEducations', () => {
        candidateEducationComponentsPage.clickOnCreateButton();
        candidateEducationDialogPage.setGradeInput('5');
        expect(candidateEducationDialogPage.getGradeInput()).toMatch('5');
        candidateEducationDialogPage.setEducationFromDateInput('2000-12-31');
        expect(candidateEducationDialogPage.getEducationFromDateInput()).toMatch('2000-12-31');
        candidateEducationDialogPage.setEducationToDateInput('2000-12-31');
        expect(candidateEducationDialogPage.getEducationToDateInput()).toMatch('2000-12-31');
        candidateEducationDialogPage.getIsPursuingEducationInput().isSelected().then((selected) => {
            if (selected) {
                candidateEducationDialogPage.getIsPursuingEducationInput().click();
                expect(candidateEducationDialogPage.getIsPursuingEducationInput().isSelected()).toBeFalsy();
            } else {
                candidateEducationDialogPage.getIsPursuingEducationInput().click();
                expect(candidateEducationDialogPage.getIsPursuingEducationInput().isSelected()).toBeTruthy();
            }
        });
        candidateEducationDialogPage.setGradeScaleInput('5');
        expect(candidateEducationDialogPage.getGradeScaleInput()).toMatch('5');
        candidateEducationDialogPage.getHighestQualificationInput().isSelected().then((selected) => {
            if (selected) {
                candidateEducationDialogPage.getHighestQualificationInput().click();
                expect(candidateEducationDialogPage.getHighestQualificationInput().isSelected()).toBeFalsy();
            } else {
                candidateEducationDialogPage.getHighestQualificationInput().click();
                expect(candidateEducationDialogPage.getHighestQualificationInput().isSelected()).toBeTruthy();
            }
        });
        candidateEducationDialogPage.setRoundOfGradeInput('5');
        expect(candidateEducationDialogPage.getRoundOfGradeInput()).toMatch('5');
        candidateEducationDialogPage.setGradeDecimalInput('5');
        expect(candidateEducationDialogPage.getGradeDecimalInput()).toMatch('5');
        candidateEducationDialogPage.setCapturedCourseInput('capturedCourse');
        expect(candidateEducationDialogPage.getCapturedCourseInput()).toMatch('capturedCourse');
        candidateEducationDialogPage.setCapturedQualificationInput('capturedQualification');
        expect(candidateEducationDialogPage.getCapturedQualificationInput()).toMatch('capturedQualification');
        candidateEducationDialogPage.setCapturedCollegeInput('capturedCollege');
        expect(candidateEducationDialogPage.getCapturedCollegeInput()).toMatch('capturedCollege');
        candidateEducationDialogPage.setCapturedUniversityInput('capturedUniversity');
        expect(candidateEducationDialogPage.getCapturedUniversityInput()).toMatch('capturedUniversity');
        candidateEducationDialogPage.setPercentageInput('5');
        expect(candidateEducationDialogPage.getPercentageInput()).toMatch('5');
        candidateEducationDialogPage.setScoreTypeInput('scoreType');
        expect(candidateEducationDialogPage.getScoreTypeInput()).toMatch('scoreType');
        candidateEducationDialogPage.setEducationDurationInput('5');
        expect(candidateEducationDialogPage.getEducationDurationInput()).toMatch('5');
        candidateEducationDialogPage.candidateSelectLastOption();
        candidateEducationDialogPage.qualificationSelectLastOption();
        candidateEducationDialogPage.courseSelectLastOption();
        candidateEducationDialogPage.collegeSelectLastOption();
        candidateEducationDialogPage.save();
        expect(candidateEducationDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CandidateEducationComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-candidate-education div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CandidateEducationDialogPage {
    modalTitle = element(by.css('h4#myCandidateEducationLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    gradeInput = element(by.css('input#field_grade'));
    educationFromDateInput = element(by.css('input#field_educationFromDate'));
    educationToDateInput = element(by.css('input#field_educationToDate'));
    isPursuingEducationInput = element(by.css('input#field_isPursuingEducation'));
    gradeScaleInput = element(by.css('input#field_gradeScale'));
    highestQualificationInput = element(by.css('input#field_highestQualification'));
    roundOfGradeInput = element(by.css('input#field_roundOfGrade'));
    gradeDecimalInput = element(by.css('input#field_gradeDecimal'));
    capturedCourseInput = element(by.css('input#field_capturedCourse'));
    capturedQualificationInput = element(by.css('input#field_capturedQualification'));
    capturedCollegeInput = element(by.css('input#field_capturedCollege'));
    capturedUniversityInput = element(by.css('input#field_capturedUniversity'));
    percentageInput = element(by.css('input#field_percentage'));
    scoreTypeInput = element(by.css('input#field_scoreType'));
    educationDurationInput = element(by.css('input#field_educationDuration'));
    candidateSelect = element(by.css('select#field_candidate'));
    qualificationSelect = element(by.css('select#field_qualification'));
    courseSelect = element(by.css('select#field_course'));
    collegeSelect = element(by.css('select#field_college'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setGradeInput = function(grade) {
        this.gradeInput.sendKeys(grade);
    };

    getGradeInput = function() {
        return this.gradeInput.getAttribute('value');
    };

    setEducationFromDateInput = function(educationFromDate) {
        this.educationFromDateInput.sendKeys(educationFromDate);
    };

    getEducationFromDateInput = function() {
        return this.educationFromDateInput.getAttribute('value');
    };

    setEducationToDateInput = function(educationToDate) {
        this.educationToDateInput.sendKeys(educationToDate);
    };

    getEducationToDateInput = function() {
        return this.educationToDateInput.getAttribute('value');
    };

    getIsPursuingEducationInput = function() {
        return this.isPursuingEducationInput;
    };
    setGradeScaleInput = function(gradeScale) {
        this.gradeScaleInput.sendKeys(gradeScale);
    };

    getGradeScaleInput = function() {
        return this.gradeScaleInput.getAttribute('value');
    };

    getHighestQualificationInput = function() {
        return this.highestQualificationInput;
    };
    setRoundOfGradeInput = function(roundOfGrade) {
        this.roundOfGradeInput.sendKeys(roundOfGrade);
    };

    getRoundOfGradeInput = function() {
        return this.roundOfGradeInput.getAttribute('value');
    };

    setGradeDecimalInput = function(gradeDecimal) {
        this.gradeDecimalInput.sendKeys(gradeDecimal);
    };

    getGradeDecimalInput = function() {
        return this.gradeDecimalInput.getAttribute('value');
    };

    setCapturedCourseInput = function(capturedCourse) {
        this.capturedCourseInput.sendKeys(capturedCourse);
    };

    getCapturedCourseInput = function() {
        return this.capturedCourseInput.getAttribute('value');
    };

    setCapturedQualificationInput = function(capturedQualification) {
        this.capturedQualificationInput.sendKeys(capturedQualification);
    };

    getCapturedQualificationInput = function() {
        return this.capturedQualificationInput.getAttribute('value');
    };

    setCapturedCollegeInput = function(capturedCollege) {
        this.capturedCollegeInput.sendKeys(capturedCollege);
    };

    getCapturedCollegeInput = function() {
        return this.capturedCollegeInput.getAttribute('value');
    };

    setCapturedUniversityInput = function(capturedUniversity) {
        this.capturedUniversityInput.sendKeys(capturedUniversity);
    };

    getCapturedUniversityInput = function() {
        return this.capturedUniversityInput.getAttribute('value');
    };

    setPercentageInput = function(percentage) {
        this.percentageInput.sendKeys(percentage);
    };

    getPercentageInput = function() {
        return this.percentageInput.getAttribute('value');
    };

    setScoreTypeInput = function(scoreType) {
        this.scoreTypeInput.sendKeys(scoreType);
    };

    getScoreTypeInput = function() {
        return this.scoreTypeInput.getAttribute('value');
    };

    setEducationDurationInput = function(educationDuration) {
        this.educationDurationInput.sendKeys(educationDuration);
    };

    getEducationDurationInput = function() {
        return this.educationDurationInput.getAttribute('value');
    };

    candidateSelectLastOption = function() {
        this.candidateSelect.all(by.tagName('option')).last().click();
    };

    candidateSelectOption = function(option) {
        this.candidateSelect.sendKeys(option);
    };

    getCandidateSelect = function() {
        return this.candidateSelect;
    };

    getCandidateSelectedOption = function() {
        return this.candidateSelect.element(by.css('option:checked')).getText();
    };

    qualificationSelectLastOption = function() {
        this.qualificationSelect.all(by.tagName('option')).last().click();
    };

    qualificationSelectOption = function(option) {
        this.qualificationSelect.sendKeys(option);
    };

    getQualificationSelect = function() {
        return this.qualificationSelect;
    };

    getQualificationSelectedOption = function() {
        return this.qualificationSelect.element(by.css('option:checked')).getText();
    };

    courseSelectLastOption = function() {
        this.courseSelect.all(by.tagName('option')).last().click();
    };

    courseSelectOption = function(option) {
        this.courseSelect.sendKeys(option);
    };

    getCourseSelect = function() {
        return this.courseSelect;
    };

    getCourseSelectedOption = function() {
        return this.courseSelect.element(by.css('option:checked')).getText();
    };

    collegeSelectLastOption = function() {
        this.collegeSelect.all(by.tagName('option')).last().click();
    };

    collegeSelectOption = function(option) {
        this.collegeSelect.sendKeys(option);
    };

    getCollegeSelect = function() {
        return this.collegeSelect;
    };

    getCollegeSelectedOption = function() {
        return this.collegeSelect.element(by.css('option:checked')).getText();
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
