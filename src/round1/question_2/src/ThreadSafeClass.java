public class ThreadSafeClass {

	public void a() {
		while (true) {
			System.out.println("a()");
		}
	}

	public synchronized void b() {
		while (true) {
			System.out.println("b()");
		}
	}

	public synchronized void c() {
		while (true) {
			System.out.println("c()");
		}
	}

	public static void d() {
		while (true) {
			System.out.println("d()");
		}
	}

	public static void e() {
		while (true) {
			System.out.println("e()");
		}
	}

	public static synchronized void f() {
		while (true) {
			System.out.println("f()");
		}
	}

	public static synchronized void g() {
		while (true) {
			System.out.println("g()");
		}
	}

	public static void main(String[] args) {
		ThreadSafeClass tsc = new ThreadSafeClass();

		// A válasz helyes, mert a metódusok törzsében nem szerepel semmi!
		
		// B válasz helyes!
//		new Thread(() -> tsc.b()).start();
//		new Thread(() -> tsc.f()).start();

		// C válasz NEM helyes!
//		new Thread(() -> tsc.b()).start();
//		new Thread(() -> tsc.c()).start();

		// D válasz NEM helyes!
//		new Thread(() -> tsc.f()).start();
//		new Thread(() -> tsc.g()).start();

		// E válasz helyes!
//		new Thread(() -> tsc.e()).start();
//		new Thread(() -> tsc.g()).start();

		// F válasz helyes!
//		new Thread(() -> tsc.a()).start();
//		new Thread(() -> tsc.a()).start();

//		new Thread(() -> tsc.a()).start();
//		new Thread(() -> tsc.b()).start();

//		new Thread(() -> tsc.a()).start();
//		new Thread(() -> tsc.c()).start();

//		new Thread(() -> tsc.a()).start();
//		new Thread(() -> tsc.d()).start();

//		new Thread(() -> tsc.a()).start();
//		new Thread(() -> tsc.e()).start();

//		new Thread(() -> tsc.a()).start();
//		new Thread(() -> tsc.f()).start();

//		new Thread(() -> tsc.a()).start();
//		new Thread(() -> tsc.g()).start();
	}

}