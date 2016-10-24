package question_18;

public class ClassToLoad {

	public static byte[] BYTE_ARRAY;

	public static ThreadLocal<ClassToLoad> THREAD_LOCAL;

	static {
		BYTE_ARRAY = new byte[1024 /** 1024*/ * 8]; // 8 KB
		THREAD_LOCAL = new ThreadLocal<>();
	}

	public ClassToLoad() {
		THREAD_LOCAL.set(this);
	}

}