/**
 * 
 */
package com.drishika.gradzcircle.service.matching;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.drishika.gradzcircle.GradzcircleApp;
import com.drishika.gradzcircle.domain.elastic.Gender;

/**
 * @author abhinav
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class JobFilterParserUnitTest {

	private JobFilterParser jobFilterParser;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		jobFilterParser = new JobFilterParser();
	}

	@Test
	public void testCreateFilterObjectWithGraduationDateAndGpa() throws Exception {
		String filterDesc = "{\"basic\":true,\"colleges\":[{\"value\":\"Nanyang Academy of Fine Arts\",\"display\":\"Nanyang Academy of Fine Arts\"}],\"premium\":true,\""
				+ "courses\":[{\"value\":\"Computer Engg.\",\"display\":\"Computer Engg.\"}],\"qualifications\":[{\"value\":\"Master\",\"display\":\"Master\"}],"
				+ "\"scoreType\":\"gpa\",\"gpa\":\"7.7\",\"addOn\":true,\"graduationDateType\":\"greater\",\"graduationDate\":{\"year\":2018,\"month\":5,\"day\":17},"
				+ "\"languages\":[{\"value\":\"Korean\",\"display\":\"Korean\"},{\"value\":\"Malay\",\"display\":\"Malay\"}],\"gender\":{\"id\":2,\"gender\":\"Female\"}}";

		Gender gender = new Gender();
		gender.setId(new Long(2));
		gender.setGender("Female");
		// System.out.println(jobFilterParser.getFilterObjectFromJson(filterDesc));
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getBasic()).isEqualTo(true);
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getColleges().size()).isEqualTo(1);
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getCourses().size()).isEqualTo(1);
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getQualifications().size()).isEqualTo(1);
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getPremium()).isEqualTo(true);
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getScoreType()).isEqualTo("gpa");
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getGpa()).isEqualTo(new Double(7.7));
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getPercentage()).isEqualTo(null);
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getAddOn()).isEqualTo(true);
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getGraduationDateType()).isEqualTo("greater");
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getGraduationDate().getGraduationDate())
				.isAfterOrEqualTo(LocalDate.of(2018, 5, 17));
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getGender().getId()).isEqualTo(gender.getId());
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getGender().getGender())
				.isEqualTo(gender.getGender());
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getLanguages().size()).isEqualTo(2);
	}

	@Test
	public void testCreateFilterObjectWithGraduationFromAndToDateAndPercent() throws Exception {
		String filterDesc = "{\"basic\":true,\"colleges\":[{\"value\":\"Nanyang Academy of Fine Arts\",\"display\":\"Nanyang Academy of Fine Arts\"}],\"premium\":true,\""
				+ "courses\":[{\"value\":\"Computer Engg.\",\"display\":\"Computer Engg.\"}],\"qualifications\":[{\"value\":\"Master\",\"display\":\"Master\"}],"
				+ "\"scoreType\":\"percent\",\"percentage\":\"90\",\"addOn\":true,\"graduationDateType\":\"between\",\"graduationFromDate\":{\"year\":2018,\"month\":6,\"day\":6},"
				+ "\"graduationToDate\":{\"year\":2018,\"month\":6,\"day\":16},"
				+ "\"gender\":{\"id\":2,\"gender\":\"Female\"}}";

		Gender gender = new Gender();
		gender.setId(new Long(2));
		gender.setGender("Female");
		// System.out.println(jobFilterParser.getFilterObjectFromJson(filterDesc));
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getBasic()).isEqualTo(true);
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getColleges().size()).isEqualTo(1);
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getCourses().size()).isEqualTo(1);
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getQualifications().size()).isEqualTo(1);
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getPremium()).isEqualTo(true);
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getScoreType()).isEqualTo("percent");
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getGpa()).isEqualTo(null);
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getPercentage()).isEqualTo(new Double(90));
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getAddOn()).isEqualTo(true);
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getGraduationDateType()).isEqualTo("between");
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getGraduationFromDate().getGraduationDate())
				.isAfterOrEqualTo(LocalDate.of(2018, 6, 6));
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getGraduationToDate().getGraduationDate())
				.isAfterOrEqualTo(LocalDate.of(2018, 6, 16));
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getGender().getId()).isEqualTo(gender.getId());
		assertThat(jobFilterParser.getFilterObjectFromJson(filterDesc).getGender().getGender())
				.isEqualTo(gender.getGender());
	}
}
