package common.util.loaders;

import lombok.SneakyThrows;

import java.util.Properties;

public class PropertyLoader {

	private static final String PROP_FILE = "/application.properties";
	public static final String VIDEO_FILE = "/video.properties";

	@SneakyThrows
	public static String loadProperty(String file, String propertyName) {
		Properties props = new Properties();
		props.load(PropertyLoader.class.getResourceAsStream(file));
		String value = null;
		if (propertyName != null) {
			value = props.getProperty(propertyName);
		}
		return value;
	}

	@SneakyThrows
	public static String loadProperty(String propertyName) {
		Properties props = new Properties();
		props.load(PropertyLoader.class.getResourceAsStream(PROP_FILE));
		String value = null;
		if (propertyName != null) {
			value = props.getProperty(propertyName);
		}
		return value;
	}

}