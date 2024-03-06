package org.hemeda3.core.encoders;

import java.util.List;

public interface Encoder {

	List<List<Double>> encode(List<String> documents);

}