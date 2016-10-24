
public class ClassToLoad {

	private static byte[] byteArray;

	static {
		byteArray = new byte[8 * 1024]; // 8 KB
	}

	public ClassToLoad() {
//		new ThreadLocal<>().s
		// TODO 
	}

	public static byte[] getByteArray() {
		return byteArray;
	}

	public static void setByteArray(byte[] byteArray) {
		ClassToLoad.byteArray = byteArray;
	}

}
