import java.util.HashMap;
import java.util.Map;

public class ThreadSleeper {

	private Map<String, Thread> registeredThreads = new HashMap<>();

	public synchronized void registerThread(String name, Thread thread) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		registeredThreads.put(name, thread);
	}

	public synchronized void sleepThread(String name, int duration) throws InterruptedException {
		if (registeredThreads.containsKey(name)) {
			System.out.println("registeredThreads.get(\"" + name + "\"): " + registeredThreads.get(name));
			registeredThreads.get(name).sleep(duration);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		// Nem Singleton, mert annyit hozok létre belőle, amennyit csak akarok!
		ThreadSleeper ts = new ThreadSleeper();
		ThreadSleeper ts2 = new ThreadSleeper();
		ThreadSleeper ts3 = new ThreadSleeper();

		ts.sleepThread(null, 250);

		String npe = "NullPointerExceptionText";
		ts.registerThread(npe, null);
		ts.sleepThread(npe, 250);

		ts.registerThread(npe, new Thread(() -> {
			System.out.println("throw new NullPointerException()");
			throw new NullPointerException();
		}));

		ts.registerThread(null, null);
	}

}