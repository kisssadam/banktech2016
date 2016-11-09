package hu.javachallenge.torpedo.util;

public class MathConstants {

	public static final double EPSILON = 0.00000001;

	/**
	 * Itt állítjuk be a küszöbértéket két kiterjesztett szonár átfedéséhez. Minél nagyobb, annál
	 * hamarabb fogja két, egymáshoz közel lévő tengeralattjáró egyszerre aktiválni a kiterjesztett
	 * szonárt. Másképpen: minél nagyobb, annál kevésbé függ egy hajó kiterjesztett szonárjának
	 * aktiválása a többi hajóétól.
	 */
	public static final double EXT_SONAR_INTERSEPT_THRESHOLD = 0.5;
	
}
