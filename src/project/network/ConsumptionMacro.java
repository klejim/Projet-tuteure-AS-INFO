package project.network;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ConsumptionMacro {

	private ConsumptionType consumpType;
	private static int Cursor;
	public HashMap<ConsumptionType,Double[]> consumpModes;
	private int tabSize; //Taille des tableaux de variat° de conso
	
	
	public ConsumptionMacro(int tabSize) {
		consumpModes=new HashMap<ConsumptionType,Double[]>();
		this.Cursor=0;
		this.tabSize=tabSize;
		
	}
			
	public void setConsumptionTab(ConsumptionType consumpType,Double[] consumpTab) {
		if(consumpTab.length==this.tabSize) {
			consumpModes.put(consumpType, consumpTab);
		}
		else {
			System.err.println("Erreur assignation tab Consommation mauvaise taille");
		}
		
	}

	public Double [] getConsumptionTab(ConsumptionType consumpType) {
		return consumpModes.get(consumpType);
	}

	public static int getCursor() {
		return Cursor;
	}

	protected static void setCursor(int cursor) {
		Cursor = cursor;
	}
	
	protected void incrementCursor() {
		Cursor=Cursor+1;
		if (Cursor>=this.tabSize) {
			Cursor=0;
		}
	}
	
	public Double getConsumFactor(ConsumptionType consumpType) {
		return this.consumpModes.get(consumpType)[Cursor];
	}
		
	
		
		
		
}