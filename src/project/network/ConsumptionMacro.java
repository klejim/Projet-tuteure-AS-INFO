package project.network;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ConsumptionMacro {

	private static int Cursor;
	public static HashMap<ConsumptionType,Double[]> consumpModes;
	private static int tabSize; //Taille des tableaux de variat° de conso
	
	static {
		consumpModes=new HashMap<ConsumptionType,Double[]>();
		Cursor=0;
		tabSize=10;
	}
	
	
	public ConsumptionMacro() {
		
	}
	
			
	public static int getTabSize() {
		return tabSize;
	}

	public static void setTabSize(int tabSize) {
		ConsumptionMacro.tabSize = tabSize;
	}

	public static void setConsumptionTab(ConsumptionType consumpType,Double[] consumpTab) {
		if(consumpTab.length==tabSize) {
			consumpModes.put(consumpType, consumpTab);
		}
		else {
			System.err.println("Erreur assignation tab Consommation mauvaise taille");
		}
		
	}

	public static Double [] getConsumptionTab(ConsumptionType consumpType) {
		return consumpModes.get(consumpType);
	}

	public static int getCursor() {
		return Cursor;
	}

	public static void setCursor(int cursor) {
		Cursor = cursor;
	}
	
	public static void incrementCursor() {
		Cursor=Cursor+1;
		if (Cursor>=tabSize) {
			Cursor=0;
		}
	}
	
	public static Double getConsumFactor(ConsumptionType consumpType) {
		return consumpModes.get(consumpType)[Cursor];
	}
	
	/**
     * Cette version permet de renvoyer un facteur de consommation futur
     * utilise un modulo pour ne pas dépasser le tableau
     * @param Type de conso, nombre d'itération d'avance
     * @see Group
     */
	
	public static Double getConsumFactor(ConsumptionType consumpType,int aheadIte) {
		return consumpModes.get(consumpType)[(Cursor+aheadIte)%tabSize];
		
	}
		
	
		
		
		
}