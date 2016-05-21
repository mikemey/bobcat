package uk.mm.bobcat.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "names")
public class NameCandidate {
	@Id
	private String id;
	@Indexed(unique = true)
	private long index;
	private String name;

	public NameCandidate(long index, String name) {
		this.index = index;
		this.name = name;
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}