package org.hemeda3.core.splitters;

import java.util.List;

public interface Splitter {

	List<List<Document>> split(List<Document> documents);

}