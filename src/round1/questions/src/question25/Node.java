package question25;

public class Node {

	private String name;
	private int value;

	public Node(String name) {
		super();
		this.name = name;
	}

	public Node(String name, int value) {
		super();
		this.name = name;
		this.value = value;
	}

	/*
	 * A hashCode azért rossz, mert nem definiálja felül a hashCode nevű
	 * metódust, mert ennek a metódusnak a nevében a 'C' karakter kicsivel van
	 * írva.
	 */
	public int hashcode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + value;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (hashCode() == other.hashCode()) // az equals ezért helytelen! Attól,
											// hogy a hashCode megegyezik, nem
											// biztos, hogy tényleg egyenlőek
											// is.
			return true;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public static void main(String[] args) {
		Node node1 = new Node("alma");
		Node node2 = new Node("alma");
		System.out.println(node1.hashCode());
		System.out.println(node1.hashCode());
		System.out.println(node2.hashCode());
		System.out.println(node1.equals(node2));
	}
	
}