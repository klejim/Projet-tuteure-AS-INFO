package project.network;

import java.util.HashMap;

public class Consumption {

	private HashMap<String,Double[]> allCTab;
	
	public Double[] testTab;
	
	public Consumption() {
		this.allCTab=new HashMap();
	}
	
	public void putTab(String name, Double[] tab) {
		
		this.allCTab.put(name, tab);
		
	}
	
	public double getIteTabValue(String name,int ite) {
		return this.allCTab.get(name)[ite];
	}
	
	public void initTestTab() {
		this.testTab=new Double[10];
		for(int i=0;i<10;i++) {
			this.testTab[i]=1+i*0.1;
		}
		
		this.putTab("test", testTab);
		
	}
	
	
	
}
