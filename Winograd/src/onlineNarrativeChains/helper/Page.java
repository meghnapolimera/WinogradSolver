package onlineNarrativeChains.helper;

class Page{
	String start;
	int label;
	
	public Page(){}
	
	public Page(String start, int label) {
		super();
		this.start = start;
		this.label = label;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}
}
