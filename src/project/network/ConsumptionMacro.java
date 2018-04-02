package project.network;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ConsumptionMacro {

	private ConsumptionType consumpType;
	private static int Cursor;
	public HashMap<ConsumptionType,Double[]> consumpModes;
	
	
	public ConsumptionMacro() {
		consumpModes=new HashMap<ConsumptionType,Double[]>();
		this.Cursor=0;
		
	}
	
	public ConsumptionMacro(HashMap<ConsumptionType,Double[]> consumpModes){
		this.Cursor=0;
		this.consumpModes=consumpModes;
	}
	
	public void setConsumptionTab(ConsumptionType consumpType,Double[] consumpTab) {
		consumpModes.put(consumpType, consumpTab);
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
	
	protected static void incrementCursor() {
		Cursor=Cursor+1;
	}
	
	public Double getConsumFactor(ConsumptionType consumpType) {
		return this.consumpModes.get(consumpType)[Cursor];
	}
		
	
		
		
		
}