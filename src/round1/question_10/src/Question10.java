public class Question10 {

	public static Integer getSomeNumber(final boolean foo, final boolean bar, final int num, final Integer num1,
			final Integer num2) {
		return foo ? num : bar ? num1 : num2;
	}

	public static void main(String[] args) {
		/*
		 * Az "A" válasz azért helyes, mert ezzel a paraméterezéssel
		 * NullPointerException-t dob
		 */
		// getSomeNumber(false, false, 0, null, null);

		/*
		 * Ebből következik, hogy a B válasz nem elfogadható, mivel van olyan
		 * paraméterezés, amely esetén kivételt dob, így a metódus nem fut le
		 * biztonságosan.
		 */

		/*
		 * A "C" válasz helyes, de nem kezdem el magyarázni, hogy miért. Lásd a
		 * csatolt képet, ill. az alábbi kimenetet:
		 * "C:\Program Files\Java\jdk1.8.0_111\bin\javap.exe" -c
		 * Question10.class
		 */

		/*
		 * A "D" válasz nem helyes, mert referenciával tér vissza. Lásd a javap
		 * program kimenetét.
		 */
	}

}
