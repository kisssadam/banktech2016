package question_18;

public class LongRunningThread {

	public static void main(String[] args) {
		new Thread(() -> {
			while (true) {
				try {
					CustomClassLoader ccl = new CustomClassLoader();
					Class<?> clazz = ccl.loadClass("question_18.ClassToLoad");
					Object object = clazz.newInstance();
					// System.out.println(clazz.getClassLoader());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}