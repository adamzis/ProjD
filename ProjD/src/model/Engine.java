package model;

public class Engine {

	private static Engine singleEngine = null;

	private Engine() {

	}

	public synchronized static Engine getEngine() {
		if (singleEngine == null)
			singleEngine = new Engine();

		return singleEngine;
	}
	
	
}
