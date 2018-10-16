package com.drishika.gradzcircle.web.rest.vm;

import com.drishika.gradzcircle.service.dto.UserDTO;
import javax.validation.constraints.Size;

import java.time.Instant;
import java.util.Set;

/**
 * View Model extending the UserDTO, which is meant to be used in the user
 * management UI.
 */
public class ManagedUserVM extends UserDTO {

	public static final int PASSWORD_MIN_LENGTH = 4;

	public static final int PASSWORD_MAX_LENGTH = 100;

	@Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
	private String password;
	private String companyName;
	private String phoneNumber;
	private String country;

	public ManagedUserVM() {
		// Empty constructor needed for Jackson.
	}

	public ManagedUserVM(Long id, String login, String password, String firstName, String lastName, String email,
			boolean activated, String imageUrl, String langKey, String createdBy, Instant createdDate,
			String lastModifiedBy, Instant lastModifiedDate, Set<String> authorities, String companyName,
			String phoneNumber, String country) {

		super(id, login, firstName, lastName, email, activated, imageUrl, langKey, createdBy, createdDate,
				lastModifiedBy, lastModifiedDate, authorities);

		this.password = password;
		this.companyName = companyName;
		this.phoneNumber = phoneNumber;
		this.country = country;

	}

	public String getPassword() {
		return password;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getCountryLocation() {
		return country;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	@Override
	public String toString() {
		return "ManagedUserVM{" + "} " + super.toString() + ", companyName='" + companyName + '\'' + ", phoneNumber="
				+ phoneNumber + ", country=" + country;
	}
}
