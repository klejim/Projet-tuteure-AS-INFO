package project.network;

import java.util.ArrayList;
import java.util.Random;

import project.network.ClusterGroup;
import project.network.ConsumptionMacro;
import project.network.Group;
import project.network.Network;
import project.network.SubStation;

public class RandomMacro {

	private static ArrayList<ClusterGroup> clusterList;
	private static final double RANDFACTOR;
	private static Random rand;
	private static final int RANDPERIOD;
	private static int randTurnLeft;

	static {
		RANDFACTOR = 0.1;
		RANDPERIOD = 3;

		rand = new Random();
		randTurnLeft = RANDPERIOD - 1;
	}

	public static void initClusterGroupAndRand(Network net) {
		ArrayList<SubStation> subStation = net.getSubStations();
		clusterList = new ArrayList<ClusterGroup>();
		for (SubStation sub : subStation) {
			ArrayList<Group> group = sub.getGroups();
			clusterList.add(new ClusterGroup(group));
		}

		applyClustersRandValue();

	}

	public static void applyClustersRandValue() {
		for (ClusterGroup cluster : clusterList) {
			cluster.setRandomFactor(getRandomGaussConsumBonus());
			cluster.applyRandomFactor();
		}

	}

	/**
	 * @return the randTurnLeft
	 */
	public static int getRandTurnLeft() {
		return randTurnLeft;
	}

	/**
	 * Renvoie une charge de consommation aléatoire à la gaussienne aléatoire
	 * @see ClusterGroup
	 */
	private static Double getRandomGaussConsumBonus() {
		return RANDFACTOR * rand.nextGaussian() + 1;

	}

	public static void routineRandom() {
		if (randTurnLeft <= 0) {
			randTurnLeft = RANDPERIOD;
			randTurnLeft--;
			applyClustersRandValue();
		}
	}
}
