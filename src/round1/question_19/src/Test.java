import java.lang.reflect.Field;

public class Test {

	public static void main(String[] args) throws Exception {
		Integer two = 2;
		Integer one = 1;

		Field twoValueField = two.getClass().getDeclaredField("value");
		twoValueField.setAccessible(true);
//		twoValueField.set(two, -2); // A c értéke ekkor -1 lesz.
//		twoValueField.set(two, 0); // A c értéke ekkor 1 lesz.
//		twoValueField.set(two, 1); // A c értéke ekkor 1 lesz.
//		twoValueField.set(two, 2); // A c értéke ekkor 3 lesz.
//		twoValueField.set(two, 3); // A c értéke ekkor 4 lesz.

		/*
		 * Ha ezt külön futtatjuk a korábbiaktól, akkor a 2-t is megkaphatjuk. A
		 * 2 módosításánál azért nem kaptunk 2-t, mert az eredmény is 2 lenne.
		 *
		 */
//		Field oneValueField = one.getClass().getDeclaredField("value");
//		oneValueField.setAccessible(true);
//		oneValueField.set(one, 0); // A c értéke ekkor 2 lesz.

		Integer a = 2;
		Integer b = 1;
		Integer c = a + b;

		System.out.println(c);
	}

}
