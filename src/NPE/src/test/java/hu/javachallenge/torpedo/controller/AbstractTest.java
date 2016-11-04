package hu.javachallenge.torpedo.controller;

import org.junit.AssumptionViolatedException;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTest {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Rule
	public TestWatcher testWatcher = new TestWatcher() {

		@Override
		protected void succeeded(Description description) {
			log.info("TEST PASSED {}", description);
		}

		@Override
		protected void failed(Throwable e, Description description) {
			log.error("TEST FAILED error: {}, description: {}", e, description);
		}

		@Override
		protected void skipped(AssumptionViolatedException e, Description description) {
			super.skipped(e, description);
			log.warn("TEST SKIPPED exception: {}, description: {}", e, description);
		}

	};

}
