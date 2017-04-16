package de.uni_koeln.spinfo.ml.toolclassification.preprocessing;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uni_koeln.spinfo.ml.toolclassification.data.BOWContainer;
import de.uni_koeln.spinfo.ml.toolclassification.data.Tool;
import de.uni_koeln.spinfo.ml.toolclassification.preprocessing.weight.TFIDFCalculator;

public class TFIDFCalculatorTest {

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

		TFIDFCalculator tfidfCalc = new TFIDFCalculator(bowContainer);

		BOWContainer weightedBow = tfidfCalc.calculateWeight();

		assertTrue("vectors dimension is still correct", weightedBow.getVectorsDim() == 7);

		bowContainer.getToolsVector().forEach((tool, vector) -> {
			assertTrue("each vector has correct length", vector.length == 7);
			switch (tool.getName()) {
			case toolName1:
				assertTrue("correct value", vector[0] == 0.10986122886681099);
				assertTrue("correct value", vector[1] == 0.08109302162163289);
				assertTrue("correct value", vector[2] == 0.0);
				assertTrue("correct value", vector[3] == 0.0);
				assertTrue("correct value", vector[4] == 0.08109302162163289);
				assertTrue("correct value", vector[5] == 0.0);
				assertTrue("correct value", vector[6] == 0.2027325540540822);
				return;
			case toolName2:
				assertTrue("correct value", vector[0] == 0.0);
				assertTrue("correct value", vector[1] == 0.0);
				assertTrue("correct value", vector[2] == 0.25341569256760277);
				assertTrue("correct value", vector[3] == 0.1013662770270411);
				assertTrue("correct value", vector[4] == 0.0);
				assertTrue("correct value", vector[5] == 0.13732653608351372);
				assertTrue("correct value", vector[6] == 0.0);
				return;
			case toolName3:
				assertTrue("correct value", vector[0] == 0.0);
				assertTrue("correct value", vector[1] == 0.08109302162163289);
				assertTrue("correct value", vector[2] == 0.04054651081081644);
				assertTrue("correct value", vector[3] == 0.08109302162163289);
				assertTrue("correct value", vector[4] == 0.08109302162163289);
				assertTrue("correct value", vector[5] == 0.0);
				assertTrue("correct value", vector[6] == 0.12163953243244931);
				return;
			}
		});
	}

}
