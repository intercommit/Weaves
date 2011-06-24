package nl.intercommit.weaves.grid;

/*
 * 
 * TODO: check Is there some existing library forthis ??
 * 
 */
public class CollectionFilter {

	private String name;
	private Object value;
	
	private OPERATOR op;
	
	public enum OPERATOR {
		EQ,
		GT,
		LT;
	}
	
	public CollectionFilter(final String name,final Object value, final OPERATOR op) {
		this.name= name;
		this.op = op;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public OPERATOR getOp() {
		return op;
	}
	
	public Object getValue() {
		return value;
	}
	
}

