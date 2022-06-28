package teste1;

public class products {
protected String name, value, description;

public products(String name, String value, String description) {
		super();
		this.name = name;
		this.value = value;
		this.description = description;
	}



public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getValue() {
	return value;
}

public void setValue(String value) {
	this.value = value;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}
}
