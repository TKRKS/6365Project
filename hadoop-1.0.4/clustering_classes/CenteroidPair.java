package org.myorg;

import java.util.*;

public class CenteroidPair {
	private String centeroidId;
	private Integer centeroidValue;
	
	public CenteroidPair(String id, Integer value) {
		centeroidId = id;
		centeroidValue = value;
	}

	public String getId() {
		return centeroidId;
	}

	public Integer getValue() {
		return centeroidValue;
	}

	public void setId(String id) {
		centeroidId = id;
	}

	public void setValue(Integer value) {
		centeroidValue = value;
	}
}
