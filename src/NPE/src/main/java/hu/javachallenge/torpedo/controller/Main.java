package hu.javachallenge.torpedo.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.javachallenge.torpedo.gui.MainPanel;
import hu.javachallenge.torpedo.service.ServiceGenerator;
import okhttp3.logging.HttpLoggingInterceptor;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);
	private static final String TEAMTOKEN = "4906CD1A4718F0B4F315BDE34B5FE430";
	private static final String TEAMNAME = "NPE";

	public static void main(String[] args) {
		log.debug("Commad line arguments: {}", Arrays.toString(args));

		if (args.length != 1) {
			log.info("Usage: java -jar NPE.jar \"<server-address>\"");
			log.info("Example usage: java -jar NPE.jar \"localhost:8080\"");
			log.error("Missing or illegal command line argument(s), exiting...");
			System.exit(1);
		}

		String serverAddress = args[0];
		log.debug("serverAddress: '{}'", serverAddress);

		if (!serverAddress.endsWith("/")) {
			log.warn("Server address doesn't end with '/' character, it will be automatically appended to it!");
			serverAddress = serverAddress + '/';
			log.debug("New server address: '{}'", serverAddress);
		}

		ServiceGenerator serviceGenerator = new ServiceGenerator(serverAddress, TEAMTOKEN, HttpLoggingInterceptor.Level.NONE);
		CallHandler callHandler = new CallHandler(serviceGenerator);

		JFrame mainFrame = new JFrame("NPE - BankTech Java Challenge 2016");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension dimension = new Dimension(1275, 640);
		mainFrame.setSize(dimension);

		BorderLayout layout = new BorderLayout();

//		JPanel statusPanel = new JPanel();
//		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
//		statusPanel.setPreferredSize(new Dimension(mainFrame.getWidth(), 20));

		MainPanel mainPanel = new MainPanel(TEAMNAME);
		mainPanel.setLayout(layout);
//		mainPanel.add(statusPanel, BorderLayout.SOUTH);
		mainFrame.add(mainPanel);
		mainFrame.setVisible(true);

		Thread gameControllerThread = new Thread(new GameController(callHandler, TEAMNAME, mainPanel));
		gameControllerThread.start();

		// TODO ha a jatek veget ert, loje ki az alkalmazas magat!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

}
