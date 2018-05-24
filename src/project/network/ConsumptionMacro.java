package project.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import project.network.RandomMacro;

/**
 * Classe servant à effectuer les variations de consommations des Groupes
 * Elle est appellée dans "Network" pour être initialisée.
 * Elle est appellée dans la classe "Group" qui lui demande le facteur de consommation de l'itération actuelle ("Cursor").
 * Pour ça elle utilise le curseur incrémentée à chaque itération pour se décaler sur le tableau de facteur du Group.
 * 
 * Il est possible d'avoir des variations différentes.
 * Les Groupes disposent d'un String identifiant à quel tableau de facteur ils doivent se référencer
 * Ce dernier point est représenté par la hashMap "consumpModes"
 * 
 * Cette classe permet également d'activer la randomisation, avec le booléen "RANDOM_ON"
 * Lorsque ce dernier est true,l'initialisation est lancée dans "init" et la routine de randomisation est activée lors de l'incrémentation du curseur.
 * Si il est sur False, il n'y a aucune action de la part de RandomMacro et le facteur de randomisation dans "Group" reste à 1
 * @author Geoffroy
 * @see RandomMacro
 * @see Group
 */
public class ConsumptionMacro {

	private static int Cursor;
	private static HashMap<String, Double[]> consumpModes;
	private static int tabSize; //Taille des tableaux de variat° de conso	
	private static boolean RANDOM_ON;

	/**
	 * Initialisation des parties n'ayant pas besoin du network	 * 
	 */
	public static void init() {
		RANDOM_ON = true;
		consumpModes = new HashMap<String, Double[]>();
		Cursor = 0;
		tabSize = 24;
	}
	
	/** 
	 * Initialisiation des parties ayant besoin du réseau
	 * Pour l'instant nécessaire uniquement pour le random
	 * 
	 * @see RandomMacro
	 */
	public static void initFromNetwork(Network net) {		
		if (RANDOM_ON) {
			RandomMacro.initClusterGroupAndRand(net);
		}
	}

	/** 
	 * @return la taille de tableau des doubles de consommations 
	 */
	public static int getTabSize() {
		return tabSize;
	}

	/** 
	 * Association des doubles de consommations et d'un String 
	 */
	public static int setConsumptionTab(String consumpType, Double... consumpTab) {

		if (consumpTab != null  && consumpTab.length == tabSize) {
			consumpModes.put(consumpType, consumpTab);
			return 1;
		}
		else if (consumpTab==null) {
			System.err.println("Erreur assignation tab Consommation nul");
			return 0;
		}
		else if (consumpTab.length != tabSize) {
			System.err.println("Erreur assignation tab Consommation mauvaise taille");
			return 0;
		}
		
		return 0;

	}

	/** 
	 * @return le tableau de consommation du consumpType 
	 */
	public static Double[] getConsumptionTab(String consumpType) {
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
	 * Passe les consommations à l'itération suivante en incrémentant le curseur
	 * Lorsque 
	 * Met à jour les valeurs de randomisation
	 */
	public static void incrementCursor() {
		Cursor++;
		if (Cursor >= tabSize) {
			Cursor = 0;
		}
		if (RANDOM_ON) {
			RandomMacro.routineRandom();
		}
		//actualisation de la randomisation
	}

	/**
	 * Cette version permet de renvoyer un facteur de consommation avec un offset.
	 * @param Type de conso, nombre d'itération d'avance
	 * @return Double de consommation de l'itération
	 * @see Group
	 */
	public static Double getConsumFactor(String consumpType, int offset) {
		try {
			int i = (Cursor + offset) % tabSize;
			if (i < 0){
				i += tabSize;
			}
			return consumpModes.get(consumpType)[i];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Demande de prediction erroné,valeur du curseur renvoyée" + e.getMessage());
			return consumpModes.get(consumpType)[Cursor];
		}
	}

	/**
	 * Cette version renvoie le facteur de consommation du curseur actuel
	 * @return Double de consommation de l'itération
	 * @see Group
	 */
	public static Double getConsumFactor(String consumpType) {
		return consumpModes.get(consumpType)[(Cursor) % tabSize];
	}

	/**
	 * Renvoie la hashmap de consommation
	 * @see Group
	 */
	public static HashMap<String, Double[]> getConsumpModes() {
		return consumpModes;
	}

}