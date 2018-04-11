package project.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ConsumptionMacro {

	private static int Cursor;
	private static HashMap<String,Double[]> consumpModes;
	private static int tabSize; //Taille des tableaux de variat° de conso
	
	
	private static final double RANDFACTOR;
	private static Random rand;
	private static final int RANDPERIOD;
	private static int randTurnLeft;
	
	private static ArrayList<ClusterGroup> clusterList;
	
	/** 
     * Initialisation 
     */
	static {
		consumpModes=new HashMap<String,Double[]>();
		Cursor=0;
		tabSize=10;
		rand=new Random();
		randTurnLeft=0;
		
		RANDFACTOR=0.1; 
		RANDPERIOD=24;
		
		
	}
	/** 
     * @return la taille des doubles de consommations 
     */
			
	public static int getTabSize() {
		return tabSize;
	}
	/** 
     * Modifie la taille (maximale?) actuelle 
     */
	public static void setTabSize(int tabSize) {
		ConsumptionMacro.tabSize = tabSize;
	}

	/** 
     * Association des doubles de consommations et d'un String 
     */
	public static int setConsumptionTab(String consumpType,Double[] consumpTab) {

		if(consumpTab.length==tabSize && consumpTab!=null) {

			consumpModes.put(consumpType, consumpTab);
			return 1;
		}
		else {
			System.err.println("Erreur assignation tab Consommation mauvaise taille");
			return 0;
		}
		
	}
	/** 
     * @return le tableau de consommation du consumpType 
     */
	public static Double [] getConsumptionTab(String consumpType) {
		return consumpModes.get(consumpType);
	}
	/** 
     * @return l'indice du curseur 
     */
	public static int getCursor() {
		return Cursor;
	}
	/** 
     * @return change le curseur des facteurs de consommations 
     */
	public static void setCursor(int cursor) {
		Cursor = cursor;
	}
	
	/** 
     * Passe les consommations à l'itération suivante 
     */
	public static void incrementCursor() {
		Cursor++;
		if (Cursor>=tabSize) {
			Cursor=0;
		}
		if(randTurnLeft<=0) {
			randTurnLeft=RANDPERIOD;
			randTurnLeft--;
		}
	}
	
	/**
     * Cette version permet de renvoyer un facteur de consommation ultérieur
     * @param Type de conso, nombre d'itération d'avance
     * @return Double de consommation de l'itération
     * @see Group
     */
	
	public static Double getConsumFactor(String consumpType,int aheadTurn) {
		return consumpModes.get(consumpType)[(Cursor+aheadTurn)%tabSize];
		
	}
		
	/**
     * Renvoie une charge de consommation aléatoire à la gaussienne aléatoire
     * @see ClusterGroup
     */	
	private static Double getRandomGaussConsumBonus() {
		return RANDFACTOR*rand.nextGaussian();
		
	}
	
	/**
     * Renvoie la hashmap de consommation
     * @see Group
     */
	public static HashMap<String, Double[]> getConsumpModes() {
		return consumpModes;
	}
	
	/**
	 * @return the randTurnLeft
	 */
	public static int getRandTurnLeft() {
		return randTurnLeft;
	}
	
	public static void setClustersRandValue() {
		for(ClusterGroup cluster: clusterList) {
			cluster.setRandomFactor(getRandomGaussConsumBonus());
		}
		
	}
	/**
	 * @param randTurnLeft the randTurnLeft to set
	 */
}