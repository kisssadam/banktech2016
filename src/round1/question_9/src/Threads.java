public class Threads {
	public static void main(String[] args) {
		new Thread(new Example(10)).start();
		new Thread(new Example(100)).start();
		new Thread(new Example(1000)).start();
	}

	static class Example implements Runnable {
		final int number;

		Example(int number) {
			this.number = number;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				System.err.println("Sleep interrupted!");
			}
			System.out.print(number + " ");
		}
	}
}