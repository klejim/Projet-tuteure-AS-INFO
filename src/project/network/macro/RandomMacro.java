package project.network.macro;

import java.util.ArrayList;
import java.util.Random;

import project.network.macro.ClusterGroup;
import project.network.macro.ConsumptionMacro;
import project.network.base.Group;
import project.network.base.Network;
import project.network.base.Network;
import project.network.base.SubStation;

/**
 * Classe Randomisant la consommation des Groupes suivant une gaussienne.
 * Chaque ClusterGroup (regroupement de groupes) possède le même facteur de randomisation.
 * Ce facteur est consommé RANDPERIOD itération et possède un écart-type de RANDFACTOR
 * 
 * La classe doit être initialisé avec initClusterGroupAndRand, qui notamment crée les ClusterGroup.
 * Cette fonction est appellé lors de l'initialisation de ConsumptionMacro si RANDOMON est true.
 * 
 * @see ClusterGroup
 * @see Group
 * @see ConsumptionMacro
 * @author Geoffroy
 */


public class RandomMacro {
	
	private static ArrayList<ClusterGroup> clusterList;
	private static final double RANDFACTOR;
	private static Random rand;
	private static final int RANDPERIOD;
	private static int randTurnLeft;

	/**
	 * Initialisation des valeurs de randomisations et de réactulisation
	 */
	static {
		RANDFACTOR = 0.1;
		RANDPERIOD = 3;
		clusterList = new ArrayList<ClusterGroup>();
		
		rand = new Random();
		randTurnLeft = RANDPERIOD - 1;
	}

	
	/**
	 * Initialisation des clustersLists, et de la première randomisation
	 * Les Groupes sont regroupés en ClusterList par stations.
	 * BIENTOT OBSOLETE
	 * @param le network concerné
	 */	
	public static ArrayList<ClusterGroup> initClusterGroupAndRand(Network net) {
		/* ancien code avant l'intégration du parser
		ArrayList<SubStation> subStation = net.getSubStation();
		

		for (SubStation sub : subStation) {
			ArrayList<Group> group = sub.getGroups();
			clusterList.add(new ClusterGroup(group));
		}
		*/
		applyClustersRandValue();
		return clusterList;
	}

	/**
	 * Applique un nouveau facteur aléatoire aux clusterGroups
	 * @see ClusterGroup
	 */
	
	public static void applyClustersRandValue() {
		for (ClusterGroup cluster : clusterList) {
			cluster.setRandomFactor(getRandomGaussConsumBonus());
			cluster.applyRandomFactor();
		}

	}

	/**
	 * @return Le nombre de tour restant avant la randomisation
	 */
	public static int getRandTurnLeft() {
		return randTurnLeft;
	}

	/**
	 * Renvoie un facteur de consommation aléatoire à la gaussienne aléatoire
	 * @see ClusterGroup
	 */
	private static Double getRandomGaussConsumBonus() {
		double factor=RANDFACTOR * rand.nextGaussian() + 1;
		if (factor<=0) {
			factor=0.001;
		}
		return factor;

	}
	/**
	 * Routine décidant si un nouveau paramètre aléatoire doit être appliqué sur les ClustersGroup
	 * @see ClusterGroup
	 */
	public static void routineRandom() {
		if (randTurnLeft <= 0) {
			randTurnLeft = RANDPERIOD;			
			applyClustersRandValue();
		}
			randTurnLeft--;
		
	}

	/**
	 * @return the clusterList
	 */
	public static ArrayList<ClusterGroup> getClusterList() {
		return clusterList;
	}
	
	
}
