package project.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * Classe permettant la lecture d'un fichier de configuration. 
 * 
 * Le parser identifie différentes sections (### nom_section ###) et va enregisrer toutes
 * les variables déclarées à l'intérieur. Les déclarations se font suivant le style de java.
 * Le parser ne reconnaît que trois types de données : les valeurs numériques (lues comme des Doubles), 
 * les chaînes de caractères et les tableaux de numériques.
 * Exemple de fichier:
 *	### DATA_CONSUMPTION ### // déclaration de section
 *	var = 15; // déclaration de variable
 *	data = [12,25.0, 2.2, 0];
 *	chaine = "Le Chat";
 * 
 *	### NETWORK_FILES ### // la syntaxe doit être respectée
 *
 *	network = "path/to/file";
 *
 *	### SECTION_2 ###
 *	testvar = 42;
 */
public class ConfigParser {
    private static final String OPTIONAL_COMMENT = "(\\s?(//.+)?)+";
    private static final String SECTION = "#{3} \\w+ #{3}" + OPTIONAL_COMMENT;
    private static final String VAR_DECLARATION = "\\w+ = .+;" + OPTIONAL_COMMENT;
    private static final String NUMERIC = "[0-9]+(\\.[0-9]+)?";
    private static final String STRING = "\"(.?)+\"";
    private static final String ARRAY = "\\[([0-9]+(\\.[0-9]+)?(,( ?)+)?)+\\]";
    private static final String OBJECTSARRAY = "\\{([\\w\\d]+(,( ?)+)?)+\\}";
    
    
    /**
     * Parcours un fichier de configuration.
     * Cette méthode crée un tableau associatif dont les clés sont les noms des variables définies dans chaque section du fichier
     * et les valeurs sont les valeurs desdites variables. Elle renvoie ensuite un second tableau dont les clés sont les noms des sections
     * et les valeurs le tableau associatif de chaque section.
     * @param filename le nom du fichier à lire
     * @return un tableau associatif contenant les variables de configuration
     * @throws java.io.FileNotFoundException fichier introuvable
     */
    public static HashMap<String, HashMap<String,Object>> parse(String filename) throws FileNotFoundException{
        HashMap<String,HashMap<String,Object>> config = new HashMap<>();
        try (Scanner scan = new Scanner(new FileInputStream(filename))) {
            String section = "DEFAULT";
            HashMap<String,Object> vars = new HashMap<>();
            while (scan.hasNext()){
                String line = scan.nextLine();
                if (line.matches(SECTION)){
                    config.put(section, vars);
                    section = line.split(" ")[1];
                    vars = new HashMap<>();
                }
                if (line.matches(VAR_DECLARATION)){
                    String splitted[] = line.split(" = ");
                    String name = splitted[0];
                    String trimmedExpression = splitted[1].trim();
                    String expression = trimmedExpression.substring(0, trimmedExpression.length()-1);
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
                        String vals[] = tmp.split(",( ?)+");
                        ArrayList<Double> values = new ArrayList<>();
                        for (String s : vals){
                            Double numeric = Double.parseDouble(s);
                            values.add(numeric);
                        }
                        addToMap(vars, name, values);
                    }
                    else if (expression.matches(OBJECTSARRAY)){
                        String tmp = expression.substring(1, expression.length()-1);
                        String vals[] = tmp.split(",( ?)+");
                        ArrayList<String> values = new ArrayList<>();
                        values.addAll(Arrays.asList(vals));
                        addToMap(vars, name, values);
                    }
                }
            }
            if (section != null){
                config.put(section, vars); // dernière section
            }
        }
        return config;
    }
    private static void addToMap(HashMap<String,Object> map, String key, Object value){
        if (!map.containsKey(key)){
                map.put(key, value);
        }
    }
    public static String stringify(HashMap<String,HashMap<String,Object>> config){
        String str = "";
        for (String s : config.keySet()){
            str += "Section " + s + "\n";
            HashMap<String,Object> configData = config.get(s);
            for (String name : config.get(s).keySet()){
                str += name + " = " + configData.get(name) + "\n";
            }
        }
        return str;
    }
    
    public static void main(String...args) throws IOException{
        HashMap<String, HashMap<String,Object>> map = ConfigParser.parse("config.txt");
        System.out.print(ConfigParser.stringify(map));
    }
}
