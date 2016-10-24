import java.io.Serializable;

public class CacheTimeoutHandler implements Serializable {

	private static final CacheTimeoutHandler INSTANCE = new CacheTimeoutHandler();
	
	private CacheTimeoutHandler() { }
	
	private volatile long lastChecked;
	
	private static final int TIMEOUT_INTERVAL = 3000;
	
	public static CacheTimeoutHandler getInstance() {
		return INSTANCE;
	}
	
	/**
	 * @return Returns true for the first time calling this method.
	 * Returns false if the cache has been living for less or equal time 
	 * than the specified TIMEOUT_INTERVAL.
	 * Otherwise, returns true.
	 */
	public boolean isTimeout() {
		final long currentTime = System.currentTimeMillis();
		if (currentTime - lastChecked > TIMEOUT_INTERVAL) {
			synchronized (this) {
				lastChecked = currentTime;
				return true;
			}
		}
		
		return false;
	}
}