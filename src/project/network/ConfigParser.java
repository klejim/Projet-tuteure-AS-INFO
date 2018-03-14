package network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileInputStream;

/**
 * 
 * 
 */
public class ConfigParser {
	private static final String SECTION = "#{3} \\w+ #{3}";
	private static final String VAR_DECLARATION = "\\w+ = (\\[|\\\")?.+(\\]|\\\")?";
	private static final String NUMERIC = "[0-9]+";
	private static final String STRING = "\"(.+ ?)+\"";
	private static final String ARRAY = "\\[([0-9]+\\.?[0-9]?,? ?)+\\]";
	public static HashMap<String, HashMap<String,Object>> parse(String filename)throws IOException{
		HashMap<String,HashMap<String,Object>> config = new HashMap<>();
		Scanner scan = new Scanner(new FileInputStream(filename));
		String section = null;
		HashMap<String,Object> vars = null;
		while (scan.hasNext()){
			String line = scan.nextLine();
			if (line.matches(SECTION)){
				if (section != null){
					config.put(section, vars);
				}
				section = line.split(" ")[1];
				vars = new HashMap<>();
			}
			if (line.matches(VAR_DECLARATION)){
				String splitted[] = line.split(" = ");
				String name = splitted[0], expression = splitted[1].substring(0, splitted[1].length()-1);
				/* cas valides pour expression
				 * numérique
				 * chaîne de caractères
				 * tableau de numériques
				 */
				if (expression.matches(NUMERIC)){
					Double value = Double.parseDouble(expression);
					addToMap(vars, name, value);
				}
				else if (expression.matches(STRING)){
					String value = expression.substring(1, expression.length()-1);
					addToMap(vars, name, value);
				}
				else if (expression.matches(ARRAY)){
					String tmp = expression.substring(1, expression.length()-1);
					String vals[] = tmp.split(", ?");
					ArrayList<Double> values = new ArrayList<>();
					for (String s : vals){
						Double numeric = Double.parseDouble(s);
						values.add(numeric);
					}
					addToMap(vars, name, values);
				}
			}
		}
		if (section != null){
			config.put(section, vars); // dernière section
		}
		scan.close();
		return config;
	}
	private static void addToMap(HashMap<String,Object> map, String key, Object value){
		if (!map.containsKey(key)){
			map.put(key, value);
		}
	}
	public static void main(String...args) throws IOException{
		HashMap<String, HashMap<String,Object>> map = ConfigParser.parse("config.txt");
		for (String s : map.keySet()){
			System.out.println("Section " + s);
			HashMap<String,Object> configData = map.get(s);
			for (String name : map.get(s).keySet()){
				System.out.println(name + " = " + configData.get(name));
			}
		}
	}
}
