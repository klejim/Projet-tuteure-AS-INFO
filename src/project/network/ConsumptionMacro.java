package project.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import project.network.RandomMacro;

public class ConsumptionMacro {

	private static int Cursor;
	private static HashMap<String,Double[]> consumpModes;
	private static int tabSize; //Taille des tableaux de variat° de conso
	
	
	
	private static boolean RANDOM_ON;
	
	
	
	/** 
     * Initialisation 
     */
	static {
		
		RANDOM_ON=false;
		
		consumpModes=new HashMap<String,Double[]>();
		Cursor=0;
		tabSize=10;
		
	}
	

	/** 
     * Initialise les fonctions ayant besoin du réseau
     * Pour l'instant nécessaire uniquement pour le random
     */
			
	public static void init(Network net) {
		if(RANDOM_ON) {
			RandomMacro.initClusterGroupAndRand(net);
		}
		
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
     * Met à jour les valeurs de randomisation
     */
	public static void incrementCursor() {
		Cursor++;
		if (Cursor>=tabSize) {
			Cursor=0;
		}
		if(RANDOM_ON) {
			RandomMacro.routineRandom();
		}
		//actualisation de la randomisation
		
		
	}
	
	/**
     * Cette version permet de renvoyer un facteur de consommation ultérieur
     * @param Type de conso, nombre d'itération d'avance
     * @return Double de consommation de l'itération
     * @see Group
     */
	
	public static Double getConsumFactor(String consumpType,int aheadTurn) {
		try {
			return consumpModes.get(consumpType)[(Cursor+aheadTurn-1)%tabSize];	
		}
		catch(ArrayIndexOutOfBoundsException e){
			System.err.println("Demande de prediction erroné,valeur du curseur renvoyée"+e.getMessage());
			return (double) 0;
			//return consumpModes.get(consumpType)[Cursor];
		}		
		
	}
	/**
     * Cette version renvoie le facteur de consommation du curseur actuel
     * @return Double de consommation de l'itération
     * @see Group
     */
	public static Double getConsumFactor(String consumpType) {		
			return consumpModes.get(consumpType)[(Cursor)%tabSize];			
		
	}
		
	
	
	/**
     * Renvoie la hashmap de consommation
     * @see Group
     */
	public static HashMap<String, Double[]> getConsumpModes() {
		return consumpModes;
	}
	
	
	
	
	
	
	
	
	
	
}