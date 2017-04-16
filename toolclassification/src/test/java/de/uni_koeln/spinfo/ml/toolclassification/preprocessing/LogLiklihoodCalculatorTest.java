package de.uni_koeln.spinfo.ml.toolclassification.preprocessing;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uni_koeln.spinfo.ml.toolclassification.data.BOWContainer;
import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.weight.LogLiklihoodCalculator;

public class LogLiklihoodCalculatorTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCalculateWeight() {
		Map<Tool, double[]> bow = new HashMap<>();

		final String toolName1 = "A";
		double[] vectorA = new double[7];
		vectorA[0] = 1;
		vectorA[1] = 2;
		vectorA[2] = 0;
		vectorA[3] = 0;
		vectorA[4] = 2;
		vectorA[5] = 0;
		vectorA[6] = 5;
		bow.put(new Tool(toolName1, null, null), vectorA);

		final String toolName2 = "B";
		double[] vectorB = new double[7];
		vectorB[0] = 0;
		vectorB[1] = 0;
		vectorB[2] = 5;
		vectorB[3] = 2;
		vectorB[4] = 0;
		vectorB[5] = 1;
		vectorB[6] = 0;
		bow.put(new Tool(toolName2, null, null), vectorB);

		final String toolName3 = "C";
		double[] vectorC = new double[7];
		vectorC[0] = 0;
		vectorC[1] = 2;
		vectorC[2] = 1;
		vectorC[3] = 2;
		vectorC[4] = 2;
		vectorC[5] = 0;
		vectorC[6] = 3;
		bow.put(new Tool(toolName3, null, null), vectorC);

		BOWContainer bowContainer = new BOWContainer(bow, 7);

		LogLiklihoodCalculator logLikelihoodCalc = new LogLiklihoodCalculator(bowContainer);

		BOWContainer weightedBow = logLikelihoodCalc.calculateWeight();

		assertTrue("vectors dimension is still correct", weightedBow.getVectorsDim() == 7);

		bowContainer.getToolsVector().forEach((tool, vector) -> {
			assertTrue("each vector has correct length", vector.length == 7);
			switch (tool.getName()) {
			case toolName1:
				assertTrue("correct value", vector[0] == 1.0296194171811583);
				assertTrue("correct value", vector[1] == 0.672944473242426);
				assertTrue("correct value", vector[2] == 0.0);
				assertTrue("correct value", vector[3] == 0.0);
				assertTrue("correct value", vector[4] == 0.672944473242426);
				assertTrue("correct value", vector[5] == 0.0);
				assertTrue("correct value", vector[6] == 2.7980789396771133);
				return;
			case toolName2:
				assertTrue("correct value", vector[0] == 0.0);
				assertTrue("correct value", vector[1] == 0.0);
				assertTrue("correct value", vector[2] == 5.352207058507067);
				assertTrue("correct value", vector[3] == 1.1192315758708453);
				assertTrue("correct value", vector[4] == 0.0);
				assertTrue("correct value", vector[5] == 1.252762968495368);
				assertTrue("correct value", vector[6] == 0.0);
				return;
			case toolName3:
				assertTrue("correct value", vector[0] == 0.0);
				assertTrue("correct value", vector[1] == 0.672944473242426);
				assertTrue("correct value", vector[2] == -0.7621400520468966);
				assertTrue("correct value", vector[3] == 0.672944473242426);
				assertTrue("correct value", vector[4] == 0.672944473242426);
				assertTrue("correct value", vector[5] == 0.0);
				assertTrue("correct value", vector[6] == 0.14637049250829615);
				return;
			}
		});
	}

}
