package de.uni_koeln.spinfo.ml.toolclassification.preprocessing.components;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator<K> implements Comparator<K> {

	Map<K, Double> base;

	public ValueComparator(Map<K, Double> base) {
		this.base = base;
	}


	public int compare(K a, K b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		}
	}

}


