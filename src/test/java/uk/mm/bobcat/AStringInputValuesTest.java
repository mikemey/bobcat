package uk.mm.bobcat;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public abstract class AStringInputValuesTest {

	protected final String inputValue;

	@Parameters
	public static Collection<String[]> data() {
		String[][] data = new String[][] { { null }, { "" }, { "  " } };
		return Arrays.asList(data);
	}

	public AStringInputValuesTest(String inputValue) {
		this.inputValue = inputValue;
	}
}