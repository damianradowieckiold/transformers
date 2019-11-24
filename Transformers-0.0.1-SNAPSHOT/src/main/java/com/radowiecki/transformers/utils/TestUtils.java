package com.radowiecki.transformers.utils;

public class TestUtils {
	public static void waitSeconds(int seconds) {
		try {
			Thread.sleep((long) (seconds * 1000));
		} catch (InterruptedException var2) {
			var2.printStackTrace();
		}

	}
}