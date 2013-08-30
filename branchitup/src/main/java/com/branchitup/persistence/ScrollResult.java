package com.branchitup.persistence;

import java.util.List;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.branchitup.json.ScrollResultsSerializer;

@JsonSerialize(using=ScrollResultsSerializer.class)
public class ScrollResult {
	public List<?> list;
	public Long count;
}