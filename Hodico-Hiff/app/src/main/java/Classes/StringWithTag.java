package Classes;

public class StringWithTag {
	private String string;
	private Object tag;

	public StringWithTag(String stringPart, Object tagPart) {
		string = stringPart;
		setTag(tagPart);
	}

	public StringWithTag() {
	}

	@Override
	public String toString() {
		return string;
	}

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}
	
	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
}