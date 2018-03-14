package network;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class ConfigParser {
	private static final String FILENAME = "config.txt";
	private static final String VAR_DECLARATION = "\\w+ = (\\[|\\\")?.+(\\]|\\\")?";
	public static HashMap<String, HashMap<String,Object>> parse()throws IOException{
		HashMap<String,HashMap<String,Object>> config = new HashMap<>();
		Scanner scan = new Scanner(new FileInputStream(FILENAME));
		String section = null;
		HashMap<String,Object> vars = null;
		while (scan.hasNext()){
			String line = scan.nextLine();
			if (line.matches("#{3} \\w+ #{3}")){
				if (section != null && !config.containsKey(section)){
					config.put(section,  vars);
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
				if (expression.matches("[0-9]+")){ // 42
					Double value = Double.parseDouble(expression);
					addToMap(vars, name, value);
				}
				else if (expression.matches("\"(\\w+ ?)+\"")){ // "chat"
					String value = expression.substring(1, expression.length()-1);
					addToMap(vars, name, value);
				}
				else if (expression.matches("\\[([0-9]+\\.?[0-9]?,? ?)+\\]")){ // [12.0, 25,32]
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
		return config;
	}
	private static void addToMap(HashMap<String,Object> map, String key, Object value){
		if (!map.containsKey(key)){
			map.put(key, value);
			
		}
	}
	public static void main(String...args) throws IOException{
		String test = "var = 12;";
		System.out.println(test.matches(VAR_DECLARATION));
		String line = "### DATA ###";
		System.out.println(line.split(" ")[1]);
		HashMap<String, HashMap<String,Object>> map = ConfigParser.parse();
		for (String s : map.keySet()){
			System.out.println("Section " + s);
			for (String name : map.get(s).keySet()){
				System.out.println(name + " = " + map.get(s).get(name));
			}
		}
	}
}
