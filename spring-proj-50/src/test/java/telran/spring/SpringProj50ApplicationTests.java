package telran.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static telran.spring.api.ApiConstant.ADD;
import static telran.spring.api.ApiConstant.CALCULATOR;
import static telran.spring.api.ApiConstant.DIV;
import static telran.spring.api.ApiConstant.MUL;
import static telran.spring.api.ApiConstant.PER;
import static telran.spring.api.ApiConstant.POW;
import static telran.spring.api.ApiConstant.SUB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.spring.api.dto.CalculateData;
import telran.spring.services.interfaces.Calculator;

@SpringBootTest
@AutoConfigureMockMvc
class SpringProj50ApplicationTests {

	Map<String, ArrayList<CalculateData>> validData = new HashMap<String, ArrayList<CalculateData>>();
	Map<String, ArrayList<CalculateData>> failData = new HashMap<String, ArrayList<CalculateData>>();
	Map<String, ArrayList<CalculateData>> otherData = new HashMap<String, ArrayList<CalculateData>>();
	String operations[] = { ADD, MUL, DIV, SUB, ADD, POW, PER };
	{
		for (String operation : operations) {
			ArrayList<CalculateData> curValid = new ArrayList<CalculateData>();
			ArrayList<CalculateData> curFail = new ArrayList<CalculateData>();
			ArrayList<CalculateData> curOther = new ArrayList<CalculateData>();

			curValid.add(new CalculateData(20, 20, operation));
			curValid.add(new CalculateData(-20, -20, operation));
			curValid.add(new CalculateData(-20, 20, operation));
			curValid.add(new CalculateData(20, -20, operation));

			curFail.add(new CalculateData(null, 20, operation));
			curFail.add(new CalculateData(20, null, operation));

			curOther.add(new CalculateData(20, 0, operation));
			curOther.add(new CalculateData(0, 20, operation));
			curOther.add(new CalculateData(-20, 0, operation));
			curOther.add(new CalculateData(0, -20, operation));
			curOther.add(new CalculateData(Integer.MAX_VALUE, Integer.MAX_VALUE, operation));
			curOther.add(new CalculateData(Integer.MIN_VALUE, Integer.MIN_VALUE, operation));
			curOther.add(new CalculateData(Integer.MIN_VALUE, Integer.MAX_VALUE, operation));
			curOther.add(new CalculateData(Integer.MAX_VALUE, Integer.MIN_VALUE, operation));
			validData.put(operation, curValid);
			failData.put(operation, curFail);
			otherData.put(operation, curOther);
		}
	}

	public void assertHttpStatusAndValue(CalculateData request, ExpectedDto expected) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(request);
		MockHttpServletResponse responce = mock
				.perform(MockMvcRequestBuilders.post(CALCULATOR).contentType(MediaType.APPLICATION_JSON).content(json))
				.andReturn().getResponse();

		assertEquals(expected.status, HttpStatus.valueOf(responce.getStatus()));
		assertEquals(expected.value, responce.getContentAsString());
	}

	class ExpectedDto {
		public String value;
		public HttpStatus status;

		public ExpectedDto(String value, HttpStatus status) {
			super();
			this.value = value;
			this.status = status;
		}

	}

	@Autowired
	Map<String, Calculator> calculators;
	@Autowired
	MockMvc mock;

	@Test
	void contextLoads() {
		assertNotNull(calculators);
	}

	@Tag("ValidParameters")
	@DisplayName("Tests with valid input params")
	@Nested
	class ValidParameters {
		@DisplayName(ADD)
		@Test
		void sum() throws Exception {
			List<ExpectedDto> expected = new ArrayList<ExpectedDto>();
			expected.add(new ExpectedDto("40", HttpStatus.OK));
			expected.add(new ExpectedDto("-40", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));

			ArrayList<CalculateData> data = validData.get(ADD);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}

		@DisplayName(SUB)
		@Test
		void sub() throws Exception {
			List<ExpectedDto> expected = new ArrayList<ExpectedDto>();
			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("-40", HttpStatus.OK));
			expected.add(new ExpectedDto("40", HttpStatus.OK));

			ArrayList<CalculateData> data = validData.get(SUB);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}

		@DisplayName(MUL)
		@Test
		void mul() throws Exception {
			List<ExpectedDto> expected = new ArrayList<ExpectedDto>();
			expected.add(new ExpectedDto("400", HttpStatus.OK));
			expected.add(new ExpectedDto("400", HttpStatus.OK));
			expected.add(new ExpectedDto("-400", HttpStatus.OK));
			expected.add(new ExpectedDto("-400", HttpStatus.OK));

			ArrayList<CalculateData> data = validData.get(MUL);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}

		@DisplayName(DIV)
		@Test
		void div() throws Exception {
			List<ExpectedDto> expected = new ArrayList<ExpectedDto>();
			expected.add(new ExpectedDto("1", HttpStatus.OK));
			expected.add(new ExpectedDto("1", HttpStatus.OK));
			expected.add(new ExpectedDto("-1", HttpStatus.OK));
			expected.add(new ExpectedDto("-1", HttpStatus.OK));

			ArrayList<CalculateData> data = validData.get(DIV);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}

		@DisplayName(POW)
		@Test
		void pow() throws Exception {
			List<ExpectedDto> expected = new ArrayList<ExpectedDto>();
			expected.add(new ExpectedDto("2147483647", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("2147483647", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));

			ArrayList<CalculateData> data = validData.get(POW);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}

		@DisplayName(PER)
		@Test
		void per() throws Exception {
			List<ExpectedDto> expected = new ArrayList<ExpectedDto>();
			expected.add(new ExpectedDto("100", HttpStatus.OK));
			expected.add(new ExpectedDto("100", HttpStatus.OK));
			expected.add(new ExpectedDto("100", HttpStatus.OK));
			expected.add(new ExpectedDto("100", HttpStatus.OK));

			ArrayList<CalculateData> data = validData.get(PER);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}
	}

	@Tag("FaildParameters")
	@DisplayName("Tests with faild input params")
	@Nested
	class FaildParameters {
		List<ExpectedDto> expected = new ArrayList<ExpectedDto>();
		{
			expected.add(new ExpectedDto("Cannot invoke \"java.lang.Integer.intValue()\" because \"data.op1\" is null",
					HttpStatus.BAD_REQUEST));
			expected.add(new ExpectedDto("Cannot invoke \"java.lang.Integer.intValue()\" because \"data.op2\" is null",
					HttpStatus.BAD_REQUEST));
		}

		@DisplayName(ADD)
		@Test
		void sum() throws Exception {
			ArrayList<CalculateData> data = failData.get(ADD);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}

		@DisplayName(SUB)
		@Test
		void sub() throws Exception {

			ArrayList<CalculateData> data = failData.get(SUB);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}

		@DisplayName(MUL)
		@Test
		void mul() throws Exception {
			ArrayList<CalculateData> data = failData.get(MUL);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}

		@DisplayName(DIV)
		@Test
		void div() throws Exception {
			ArrayList<CalculateData> data = failData.get(DIV);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}

		@DisplayName(POW)
		@Test
		void pow() throws Exception {
			ArrayList<CalculateData> data = failData.get(POW);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}

		@DisplayName(PER)
		@Test
		void per() throws Exception {
			ArrayList<CalculateData> data = failData.get(PER);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}
	}

	@Tag("MayFailParameters")
	@DisplayName("Tests with valid input params, but with exceptions")
	@Nested
	class MayFailParameters {
		@DisplayName(ADD)
		@Test
		void sum() throws Exception {
			List<ExpectedDto> expected = new ArrayList<ExpectedDto>();
			expected.add(new ExpectedDto("20", HttpStatus.OK));
			expected.add(new ExpectedDto("20", HttpStatus.OK));
			expected.add(new ExpectedDto("-20", HttpStatus.OK));
			expected.add(new ExpectedDto("-20", HttpStatus.OK));

			expected.add(new ExpectedDto("-2", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("-1", HttpStatus.OK));
			expected.add(new ExpectedDto("-1", HttpStatus.OK));

			ArrayList<CalculateData> data = otherData.get(ADD);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}

		@DisplayName(SUB)
		@Test
		void sub() throws Exception {
			List<ExpectedDto> expected = new ArrayList<ExpectedDto>();
			expected.add(new ExpectedDto("20", HttpStatus.OK));
			expected.add(new ExpectedDto("-20", HttpStatus.OK));
			expected.add(new ExpectedDto("-20", HttpStatus.OK));
			expected.add(new ExpectedDto("20", HttpStatus.OK));

			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("1", HttpStatus.OK));
			expected.add(new ExpectedDto("-1", HttpStatus.OK));

			ArrayList<CalculateData> data = otherData.get(SUB);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}

		@DisplayName(MUL)
		@Test
		void mul() throws Exception {
			List<ExpectedDto> expected = new ArrayList<ExpectedDto>();
			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));

			expected.add(new ExpectedDto("1", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("-2147483648", HttpStatus.OK));
			expected.add(new ExpectedDto("-2147483648", HttpStatus.OK));

			ArrayList<CalculateData> data = otherData.get(MUL);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}

		@DisplayName(DIV)
		@Test
		void div() throws Exception {
			List<ExpectedDto> expected = new ArrayList<ExpectedDto>();
			expected.add(new ExpectedDto("/ by zero", HttpStatus.BAD_REQUEST));
			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("/ by zero", HttpStatus.BAD_REQUEST));
			expected.add(new ExpectedDto("0", HttpStatus.OK));

			expected.add(new ExpectedDto("1", HttpStatus.OK));
			expected.add(new ExpectedDto("1", HttpStatus.OK));
			expected.add(new ExpectedDto("-1", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));

			ArrayList<CalculateData> data = otherData.get(DIV);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}

		@DisplayName(POW)
		@Test
		void pow() throws Exception {
			List<ExpectedDto> expected = new ArrayList<ExpectedDto>();
			expected.add(new ExpectedDto("1", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("1", HttpStatus.OK));
			expected.add(new ExpectedDto("2147483647", HttpStatus.OK));

			expected.add(new ExpectedDto("2147483647", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("-2147483648", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));

			ArrayList<CalculateData> data = otherData.get(POW);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}

		@DisplayName(PER)
		@Test
		void per() throws Exception {
			List<ExpectedDto> expected = new ArrayList<ExpectedDto>();
			expected.add(new ExpectedDto("/ by zero", HttpStatus.BAD_REQUEST));
			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("/ by zero", HttpStatus.BAD_REQUEST));
			expected.add(new ExpectedDto("0", HttpStatus.OK));

			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));
			expected.add(new ExpectedDto("0", HttpStatus.OK));

			ArrayList<CalculateData> data = otherData.get(PER);
			for (int i = 0; i < data.size(); i++) {
				assertHttpStatusAndValue(data.get(i), expected.get(i));
			}
		}
	}

}
