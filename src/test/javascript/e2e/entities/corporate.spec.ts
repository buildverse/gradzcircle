import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Corporate e2e test', () => {

    let navBarPage: NavBarPage;
    let corporateDialogPage: CorporateDialogPage;
    let corporateComponentsPage: CorporateComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Corporates', () => {
        navBarPage.goToEntity('corporate');
        corporateComponentsPage = new CorporateComponentsPage();
        expect(corporateComponentsPage.getTitle())
            .toMatch(/gradzcircleApp.corporate.home.title/);

    });

    it('should load create Corporate dialog', () => {
        corporateComponentsPage.clickOnCreateButton();
        corporateDialogPage = new CorporateDialogPage();
        expect(corporateDialogPage.getModalTitle())
            .toMatch(/gradzcircleApp.corporate.home.createOrEditLabel/);
        corporateDialogPage.close();
    });

    it('should create and save Corporates', () => {
        corporateComponentsPage.clickOnCreateButton();
        corporateDialogPage.setNameInput('name');
        expect(corporateDialogPage.getNameInput()).toMatch('name');
        corporateDialogPage.setAddressInput('address');
        expect(corporateDialogPage.getAddressInput()).toMatch('address');
        corporateDialogPage.setCityInput('city');
        expect(corporateDialogPage.getCityInput()).toMatch('city');
        corporateDialogPage.setEstablishedSinceInput('2000-12-31');
        expect(corporateDialogPage.getEstablishedSinceInput()).toMatch('2000-12-31');
        corporateDialogPage.setEmailInput('email');
        expect(corporateDialogPage.getEmailInput()).toMatch('email');
        corporateDialogPage.setOverviewInput('overview');
        expect(corporateDialogPage.getOverviewInput()).toMatch('overview');
        corporateDialogPage.setBenefitsInput('benefits');
        expect(corporateDialogPage.getBenefitsInput()).toMatch('benefits');
        corporateDialogPage.setWebsiteInput('website');
        expect(corporateDialogPage.getWebsiteInput()).toMatch('website');
        corporateDialogPage.setFacebookInput('facebook');
        expect(corporateDialogPage.getFacebookInput()).toMatch('facebook');
        corporateDialogPage.setTwitterInput('twitter');
        expect(corporateDialogPage.getTwitterInput()).toMatch('twitter');
        corporateDialogPage.setInstagramInput('instagram');
        expect(corporateDialogPage.getInstagramInput()).toMatch('instagram');
        corporateDialogPage.setLinkedInInput('linkedIn');
        expect(corporateDialogPage.getLinkedInInput()).toMatch('linkedIn');
        corporateDialogPage.setCultureInput('culture');
        expect(corporateDialogPage.getCultureInput()).toMatch('culture');
        corporateDialogPage.setContactPersonInput('contactPerson');
        expect(corporateDialogPage.getContactPersonInput()).toMatch('contactPerson');
        corporateDialogPage.setPhoneInput('phone');
        expect(corporateDialogPage.getPhoneInput()).toMatch('phone');
        corporateDialogPage.setPhoneCodeInput('phoneCode');
        expect(corporateDialogPage.getPhoneCodeInput()).toMatch('phoneCode');
        corporateDialogPage.setPersonDesignationInput('personDesignation');
        expect(corporateDialogPage.getPersonDesignationInput()).toMatch('personDesignation');
        corporateDialogPage.setTagLineInput('tagLine');
        expect(corporateDialogPage.getTagLineInput()).toMatch('tagLine');
        corporateDialogPage.setEscrowAmountInput('5');
        expect(corporateDialogPage.getEscrowAmountInput()).toMatch('5');
        corporateDialogPage.countrySelectLastOption();
        corporateDialogPage.industrySelectLastOption();
        corporateDialogPage.loginSelectLastOption();
        corporateDialogPage.save();
        expect(corporateDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CorporateComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-corporate div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CorporateDialogPage {
    modalTitle = element(by.css('h4#myCorporateLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    addressInput = element(by.css('input#field_address'));
    cityInput = element(by.css('input#field_city'));
    establishedSinceInput = element(by.css('input#field_establishedSince'));
    emailInput = element(by.css('input#field_email'));
    overviewInput = element(by.css('input#field_overview'));
    benefitsInput = element(by.css('input#field_benefits'));
    websiteInput = element(by.css('input#field_website'));
    facebookInput = element(by.css('input#field_facebook'));
    twitterInput = element(by.css('input#field_twitter'));
    instagramInput = element(by.css('input#field_instagram'));
    linkedInInput = element(by.css('input#field_linkedIn'));
    cultureInput = element(by.css('input#field_culture'));
    contactPersonInput = element(by.css('input#field_contactPerson'));
    phoneInput = element(by.css('input#field_phone'));
    phoneCodeInput = element(by.css('input#field_phoneCode'));
    personDesignationInput = element(by.css('input#field_personDesignation'));
    tagLineInput = element(by.css('input#field_tagLine'));
    escrowAmountInput = element(by.css('input#field_escrowAmount'));
    countrySelect = element(by.css('select#field_country'));
    industrySelect = element(by.css('select#field_industry'));
    loginSelect = element(by.css('select#field_login'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNameInput = function(name) {
        this.nameInput.sendKeys(name);
    };

    getNameInput = function() {
        return this.nameInput.getAttribute('value');
    };

    setAddressInput = function(address) {
        this.addressInput.sendKeys(address);
    };

    getAddressInput = function() {
        return this.addressInput.getAttribute('value');
    };

    setCityInput = function(city) {
        this.cityInput.sendKeys(city);
    };

    getCityInput = function() {
        return this.cityInput.getAttribute('value');
    };

    setEstablishedSinceInput = function(establishedSince) {
        this.establishedSinceInput.sendKeys(establishedSince);
    };

    getEstablishedSinceInput = function() {
        return this.establishedSinceInput.getAttribute('value');
    };

    setEmailInput = function(email) {
        this.emailInput.sendKeys(email);
    };

    getEmailInput = function() {
        return this.emailInput.getAttribute('value');
    };

    setOverviewInput = function(overview) {
        this.overviewInput.sendKeys(overview);
    };

    getOverviewInput = function() {
        return this.overviewInput.getAttribute('value');
    };

    setBenefitsInput = function(benefits) {
        this.benefitsInput.sendKeys(benefits);
    };

    getBenefitsInput = function() {
        return this.benefitsInput.getAttribute('value');
    };

    setWebsiteInput = function(website) {
        this.websiteInput.sendKeys(website);
    };

    getWebsiteInput = function() {
        return this.websiteInput.getAttribute('value');
    };

    setFacebookInput = function(facebook) {
        this.facebookInput.sendKeys(facebook);
    };

    getFacebookInput = function() {
        return this.facebookInput.getAttribute('value');
    };

    setTwitterInput = function(twitter) {
        this.twitterInput.sendKeys(twitter);
    };

    getTwitterInput = function() {
        return this.twitterInput.getAttribute('value');
    };

    setInstagramInput = function(instagram) {
        this.instagramInput.sendKeys(instagram);
    };

    getInstagramInput = function() {
        return this.instagramInput.getAttribute('value');
    };

    setLinkedInInput = function(linkedIn) {
        this.linkedInInput.sendKeys(linkedIn);
    };

    getLinkedInInput = function() {
        return this.linkedInInput.getAttribute('value');
    };

    setCultureInput = function(culture) {
        this.cultureInput.sendKeys(culture);
    };

    getCultureInput = function() {
        return this.cultureInput.getAttribute('value');
    };

    setContactPersonInput = function(contactPerson) {
        this.contactPersonInput.sendKeys(contactPerson);
    };

    getContactPersonInput = function() {
        return this.contactPersonInput.getAttribute('value');
    };

    setPhoneInput = function(phone) {
        this.phoneInput.sendKeys(phone);
    };

    getPhoneInput = function() {
        return this.phoneInput.getAttribute('value');
    };

    setPhoneCodeInput = function(phoneCode) {
        this.phoneCodeInput.sendKeys(phoneCode);
    };

    getPhoneCodeInput = function() {
        return this.phoneCodeInput.getAttribute('value');
    };

    setPersonDesignationInput = function(personDesignation) {
        this.personDesignationInput.sendKeys(personDesignation);
    };

    getPersonDesignationInput = function() {
        return this.personDesignationInput.getAttribute('value');
    };

    setTagLineInput = function(tagLine) {
        this.tagLineInput.sendKeys(tagLine);
    };

    getTagLineInput = function() {
        return this.tagLineInput.getAttribute('value');
    };

    setEscrowAmountInput = function(escrowAmount) {
        this.escrowAmountInput.sendKeys(escrowAmount);
    };

    getEscrowAmountInput = function() {
        return this.escrowAmountInput.getAttribute('value');
    };

    countrySelectLastOption = function() {
        this.countrySelect.all(by.tagName('option')).last().click();
    };

    countrySelectOption = function(option) {
        this.countrySelect.sendKeys(option);
    };

    getCountrySelect = function() {
        return this.countrySelect;
    };

    getCountrySelectedOption = function() {
        return this.countrySelect.element(by.css('option:checked')).getText();
    };

    industrySelectLastOption = function() {
        this.industrySelect.all(by.tagName('option')).last().click();
    };

    industrySelectOption = function(option) {
        this.industrySelect.sendKeys(option);
    };

    getIndustrySelect = function() {
        return this.industrySelect;
    };

    getIndustrySelectedOption = function() {
        return this.industrySelect.element(by.css('option:checked')).getText();
    };

    loginSelectLastOption = function() {
        this.loginSelect.all(by.tagName('option')).last().click();
    };

    loginSelectOption = function(option) {
        this.loginSelect.sendKeys(option);
    };

    getLoginSelect = function() {
        return this.loginSelect;
    };

    getLoginSelectedOption = function() {
        return this.loginSelect.element(by.css('option:checked')).getText();
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
