package project.network;

import java.awt.List;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import project.util.ConfigParser;
import project.util.SortedArrayList;

/**
 * Classe représentant le réseau de distribution.
 * 
 * Elle s'occupe de la gestion du réseau (création, réaction aux évolutions,...) et sert de dialogue avec l'extérieur. 
 * @author Jimenez
 */
public class Network {
    private HashMap<String, HashMap<String, Object>> config; // contenu du fichier config
    private int production, consumption;
    private SortedArrayList<Node> nodes;

    public Network() throws FileNotFoundException {
    	ConsumptionMacro.init();   	
    	    	
        nodes = new SortedArrayList<>(Node.comparator);
        
        //Lecture du fichier de parsing du réseau
        config = ConfigParser.parse("config");
        System.out.print(ConfigParser.stringify(config));
        String networkFile = (String) config.get("NETWORK_FILES").get("network");
        
        //Lecture et assignation du tableau de variation de consommation
        if (config.containsKey("DATA_CONSUMPTION")) {
            HashMap<String, Object> data = config.get("DATA_CONSUMPTION");
            for (String varName : data.keySet()) {
                Object var = data.get(varName);
                System.out.println(var.getClass());
                
                if (var instanceof ArrayList) {
                    ArrayList<Double> tmp = (ArrayList<Double>) var;
                    Double tab[] = new Double[tmp.size()];
                    for (int i = 0; i < tmp.size(); i++) {
                    	tab[i] = tmp.get(i);
                    }
                    
                    ConsumptionMacro.setConsumptionTab(varName, tab);
                    
                     
                }
                
            }
        }
        //mise en place du réseau dans setupNetwork
			try {
				setupNetwork(ConfigParser.parse(networkFile));
			} catch (RuntimeException e) {				
				e.printStackTrace();				
				System.exit(0);
			}
        
    }

    /**
     * Initialisation du réseau à partir de la valeur de config.
     * 
     * Description de l'execution :
     * 1)On parcours la hashmap network pour ajouter les nodes au network
     * 2)Pendant ce parcours on répertorie les Groups et PowerPlants par nom, et on répertorie les SubStations avec les nodes qui y sont connectés
     * 3)Une fois le premier parcours terminé, on parcours cette liste de SubStation/Nodes en vue de faire le lien avec la liste de Group/nom et PowerPlant/nom
     * et de créer le lien à la sous-station s'il y'a lieu.
     * 
     * @param network un tableau associatif représentant le réseau.
     * @throws Exception 
     * @see ConfigParser
     * @author Geoffroy
     */
   
    private void setupNetwork(HashMap<String, HashMap<String, Object>> network) throws RuntimeException {
    	//HashMaps destinés à stocker les nodes en vue de leur liason dans les sous-stations (après le for)
    	HashMap<String,Group> groupsMap =new HashMap<String, Group>();
    	HashMap<String,PowerPlant> powerpMap =new HashMap<String, PowerPlant>();
    	HashMap<SubStation,ArrayList<String>> subsMap =new HashMap<SubStation, ArrayList<String>>();
    	
        for (String key : network.keySet()) {
        	if (key.matches("GROUP_.+")) {            	
                HashMap<String, Object> group = network.get(key);               
                
                if (group.containsKey("name") && group.containsKey("consumption")) {
                    String name = (String) group.get("name");
                    
                    int consumption=(((Double) group.get("consumption"))).intValue();
                    Group g=new Group(consumption, name, "test");
                    this.nodes.add(g);
                    groupsMap.put(key, g);
                    int idCluster=0;
                    if(group.containsKey("clustergroup")) {
                    	idCluster=(((Double) group.get("clustergroup"))).intValue();
                    }
                    	
                	int clusterFound=0;
                	for (ClusterGroup cg : RandomMacro.getClusterList()) {                    		
                		if (cg.getId()==idCluster) {
                			cg.getGroupList().add(g);
                			clusterFound=1;
                		}
                	}
                	if(clusterFound==0) {
                		ClusterGroup clusterObj=new ClusterGroup(g,idCluster);
                		RandomMacro.getClusterList().add(clusterObj);                    		
                	}
                    
                }                 
                else {
                	throw new RuntimeException("Un groupe n'est pas correctement configuré dans le fichier network: "+key);
                }
            }
            else if (key.matches("PLANT_.+")) {
            	HashMap<String, Object> plant = network.get(key);
            	
            	if(plant.containsKey("name") && plant.containsKey("type")) {
        		String type =(String) plant.get("type");            		
        			String name = (String) plant.get("name");
                    switch(type) {
                    case "NUCLEAR" :
                    	NuclearPlant np=new NuclearPlant(name);
                    	this.nodes.add(np);
                    	powerpMap.put(key, np);
                    	break;
                    case "HYDRAULIC" :
                    	HydraulicPlant powerp=new HydraulicPlant(name);
                    	this.nodes.add(powerp);
                    	powerpMap.put(key, powerp);
                    	break;
                    case "GAS" :
                    	GasPlant gasp=new GasPlant(name);
                    	this.nodes.add(gasp);
                    	powerpMap.put(key, gasp);
                    	break;
                    
                    default :
                    	throw new RuntimeException("Une Centrale n'est pas correctement configurée dans le fichier network (type mauvais): " +key);
                    }
                
            	}
            	else {
            		throw new RuntimeException("Une Centrale n'est pas correctement configurée dans le fichier network (champs obligatoires non présents) : " +key);
            	}
            		
            }
            else if (key.matches("SUBSTATION_.+")) {
            	HashMap<String, Object> substation = network.get(key);
            	if(substation.containsKey("name")&&substation.containsKey("groups")&&substation.containsKey("plants")) {
            		String name = (String) substation.get("name");
            		SubStation ss=new SubStation(name);
            		this.nodes.add(ss);
            		
            		ArrayList<String> groupsSubsList=(ArrayList<String>) substation.get("groups");
            		ArrayList<String> plantsSubsList=(ArrayList<String>) substation.get("plants");
            		ArrayList<String> nodeSubsList= new ArrayList<String>();
            		nodeSubsList.addAll(groupsSubsList);
            		nodeSubsList.addAll(plantsSubsList);
            		subsMap.put(ss, nodeSubsList);           		
            		            		
            	}
            	else {
            		throw new RuntimeException("Une Sous-Station n'est pas correctement configurée dans le fichier network: "+key);
            	}
            	
            	
            }
            
       }
       //1er parcours terminé on connecte maintenant les nodes aux sous stations
       for(Map.Entry singleSub : subsMap.entrySet()) {
    	   ArrayList<String> nodeList=(ArrayList<String>) singleSub.getValue();
    	   SubStation sub=(SubStation) singleSub.getKey();
    	   for(String s : nodeList) {
    		   if(s.matches("GROUP_.+")) {
    			   if(groupsMap.containsKey(s)){
    				   this.addGroupsToStation(sub,groupsMap.get(s));
        			   groupsMap.remove(s);
    			   }
    			   else{
    				   throw new RuntimeException("Un même groupe est configuré en double dans le fichier network :" +s);
    			   }    			   
    		   }    		   
    		   else if(s.matches("PLANT_.+")) {
       			   if(powerpMap.containsKey(s)){
       				   PowerPlant pp=powerpMap.get(s);
       				   if(!sub.getPlants().contains(pp)){
       					   this.addPlantsToStation(sub, pp);
       				   }
       				   else{
       					   throw new RuntimeException("Une centrale est configurée en double sur une même sous-station" +s);
       				   }
       			   }       			   
       			}
    		   else{
    			   throw new RuntimeException("Une clé est de sous-station est de nature inconnue :" +s);
    		   }
    	   }
       }
       //Initialisation des paramétres nécessitant les nodes du réseau mises en places (en réalité seulement l'initialisation de l'aléatoire)
       ConsumptionMacro.initFromNetwork(this);
    }

    /**
     * Analyse le réseau à la recherche d'erreurs.
     * @return la liste des erreurs rencontrées.
     */
    public ArrayList<NetworkError> analyze() {
        ArrayList<NetworkError> errors = new ArrayList<>();
        for (Node n : nodes) {
            if (!n.isConnected()) {
                errors.add(new NodeNotConnectedError(n));
            }
            if (n instanceof SubStation) {
                SubStation station = (SubStation) n;
                if (station.getDiff() != 0){
                    errors.add(analyseSubStation(station)); /* /!\ peut renvoyer null! */
                }
                errors.add(new NeedOptimizationError(station)); // TODO : être intelligent
            }
        }
        errors.removeIf(e->e==null); // important
        return errors;
    }
    /**
     * Analyse l'équilibre des puissances d'une sous-station et retourne l'erreur appropriée. 
     * prérequis : station.getDiff() != 0
     * @param station la sous-station concernée
     * @return l'instance de NetworkError appropriée
     */
    public NetworkError analyseSubStation(SubStation station){
        int diff = station.getDiff();
        NetworkError error = null;
        if (diff > 0){
            error = new TooMuchPowerError(station, diff);
        } else{
            int powerNeeded = Math.abs(diff);
            /*
            * pour le cas où la résolution d'une erreur demande le démarrage d'une centrale
            * il est généralement nécessaire d'attendre plusieurs itérations avant la
            * résolution. Pour cette raison on doit vérifier que la puissance exigée n'est pas déjà affectée 
            */
            // gestion des lignes en attente : si l'erreur peut être résolue par les puissances venant de centrales en démarrage on considère
            // qu'il est inutile de la générer
            // TODO : amélioration possible pour le cas où l'attente serait plus longue que démarrer une autre centrale
            // quoique a priori l'algo démarre déjà les centrales les plus rapides, donc à voir. Ca peut être utile si les consommation varient énormément
        for (Line line : station.getLines()) {
                if (line.getState() == Line.State.WAITING){
                    powerNeeded -= line.getPower();
                }
            }
            // si ce n'est pas suffisant on génère une erreur pour la différence
            if (powerNeeded > 0){
                error = new NotEnoughPowerError(station, powerNeeded);
            }
        }
        return error;
    }
    /**
     * Gestion prédictive. 
     * prédiction à:
     * 1 itération : on applique la gestion des erreurs normale
     * 5 itérations : 
     * 20 itérations :
     */
    public void predictFutureAndReact(){
        ArrayList<SubStation> stations = getSubStations();
        ArrayList<NetworkError> errors = new ArrayList<>();
        // voyage dans le futur
        for (SubStation station : stations){
            // 1 itération
            for (Group group: station.getGroups()){
                group.setConsumption((int)(group.getConsumptionWithOffset(1)*1.10));
            }
            station.updateOutput();
            if (station.getDiff() != 0){
                errors.add(analyseSubStation(station));
            }
        }
        errors.removeIf(e->e == null); // important car analyzeSubStation peut retourner null
        handleErrors(errors);
        // retour à l'itération courante
        for (SubStation station : stations){
            for (Group group : station.getGroups()){
                group.setConsumption(group.getConsumptionWithOffset(0));
            }
            station.updateOutput();
        }
    }

    /**
     * Traitement des erreurs générées par {@link #analyze() analyze}. Pour chaque erreur une solution est cherchée. Si aucune n'est trouvée
     * on génère une nouvelle erreur qui est une instance de {@link CannotFindSolutionError CannotFindSolutionError}. Si on trouve une
     * solution on marque l'erreur comme résolue. Dans tous les cas les erreurs sont toutes renvoyées. 
     * @param rawErrors liste des erreurs brutes générées par l'analyse.
     * @return liste d'erreurs définitive.
     * @see #solvePowerOverflow(project.network.SubStation, int)
     * @see #solvePowerShortage(project.network.SubStation, int) 
     */
    public ArrayList<NetworkError> handleErrors(ArrayList<NetworkError> rawErrors) {
        ArrayList<NetworkError> errors = new ArrayList<>(rawErrors.size());
        rawErrors.sort(NetworkError.typeAndPowerComparator);
        errors.addAll(rawErrors);
        for (NetworkError error : rawErrors) {
            error.solve();
            if (!error.isSolved()){
                CannotFindSolutionError newError = new CannotFindSolutionError(error);
                errors.add(newError);
            }
        }
        return errors;
    }

    /**
     * Réalise une itération du réseau.
     * 
     * <p>Les étapes de l'itération sont les suivantes :</p>
     * <ul>
     * <li>Faire avancer les modes de consommation (incrémenter leurs curseurs internes)</li>
     * <li>Mettre à jour les groupes</li>
     * <li>Mettre à jour les sous-station</li>
     * <li>Mettre à jour les attributs de l'instance de Network</li>
     * <li>Analyser le réseau</li>
     * <li>Gérer les erreurs</li>
     * <li>Renvoyer le résultat</li>
     * </ul>
     * <p>Cette fonction se charge de la mise à jour (étapes 1 à 4).</p>
     * @see #analyze() 
     * @see #handleErrors(java.util.ArrayList) 
     */
    public void update() {
        // remarque : on suppose que la puissance de base d'une centrale est constante. Si cela change dans le futur il faudra modiier
        // la gestion des centrales et de leur puissance disponible.
        // nodes étant trié le parcours suivant met à jour tous les groupes, puis toutes centrales et enfin les sous-stations
        consumption = 0;
        production = 0;
        ConsumptionMacro.incrementCursor();
        for (Node node : nodes) {
            node.update();
            if (node instanceof SubStation) {
                consumption += ((SubStation) node).getPowerOut();
                production += ((SubStation) node).getPowerIn();
            }
        }
    }

    /**
     * Initialise un réseau relativement simple.
     */
    private void TESTInitNetwork() {

        /*Double [] consumpTab=new Double [10];
        for(int i=0;i<10;i++) {
        	consumpTab[i]= (double) (1+i*0.1);
        }
        ConsumptionMacro.setConsumptionTab("test", consumpTab);*/

        NuclearPlant np1 = new NuclearPlant("nucléaire 1"), np2 = new NuclearPlant("nucléaire 2"), np3 = new NuclearPlant("nucléaire 3");
        HydraulicPlant hp1 = new HydraulicPlant("hydraulique 1"), hp2 = new HydraulicPlant("hydraulique 2");
        GasPlant gp1 = new GasPlant("gaz 1");
        SubStation s1 = new SubStation("station 1"), s2 = new SubStation("station 2"), s3 = new SubStation("station 3");
        Group g1 = new Group(500000, "groupe 1", "test"), g2 = new Group(300000, "groupe 2", "test"),
                g3 = new Group(200000, "groupe 3", "test");
        Group g4 = new Group(100000, "groupe 4", "test"), g5 = new Group(200000, "groupe 5", "test"),
                g6 = new Group(250000, "groupe 6", "test");
        Group g7 = new Group(250000, "groupe 7", "test"), g8 = new Group(250000, "groupe 8", "test");
        Node tab[] = { np1, np2, np3, hp1, hp2, s1, s2, s3, g1, g2, g3, g4, g5, g6, g7, gp1, g8 };
        nodes.addAll(Arrays.asList(tab));
        addGroupsToStation(s1, g1, g2);
        addGroupsToStation(s2, g3, g4, g5);
        addGroupsToStation(s3, g6, g7);
        addPlantsToStation(s1, np1, gp1);
        addPlantsToStation(s2, np2, gp1, hp2);
        addPlantsToStation(s3, np3, hp2);
        ConsumptionMacro.initFromNetwork(this);

    }

    /**
     * Regroupe les étapes nécessaires à l'ajout de groupes de consommation à une sous-station.
     * @param s la sous-station à laquelle ajouter les groupes.
     * @param gr les groupes de consommation à ajouter.
     */
    public void addGroupsToStation(SubStation s, Group... gr) {
        for (Group g : gr) {
            g.setStation(s);
        }
        s.addGroups(gr);
        s.updateOutput();
        computeConsumption();
    }

    /**
     * Regroupe les étapes nécessaires à l'ajout de centrales à une sous-station.
     * @param s la sous-station à laquelle ajouter les centrales.
     * @param plants les centrales à ajouter.
     */
    public void addPlantsToStation(SubStation s, PowerPlant... plants) {
        for (PowerPlant p : plants) {
            Line line = new Line(p, s);
            p.addLines(line);
            s.addLines(line);
        }
        computeProduction();
    }

    /**
     * Met à jour la consommation totale du réseau en fonction des besoins de chaque sous-station.
     */
    public void computeConsumption() {
        consumption = 0;
        for (Node n : nodes) {
            if (n instanceof SubStation) {
                consumption += ((SubStation) n).getPowerOut();
            }
        }
    }

    /**
     * Met à jour la production totale du réseau à partir de la puissance reçue par chaque sous-station.
     */
    public void computeProduction() {
        production = 0;
        for (Node n : nodes) {
            if (n instanceof SubStation) {
                production += ((SubStation) n).getPowerIn();
            }
        }
    }

    /**
     * Compte le nombre d'instances des classes données en paramètre dans le tableau nodes.
     * @param classes les classes dont on veut compter les instances.
     * @return un tableau contenant le nombre d'instance de chaque classe.
     */
    public int[] count(Class... classes) {
        int numbers[] = new int[classes.length];
        for (Node n : nodes) {
            for (int i = 0; i < classes.length; i++) {
                Class c = classes[i];
                if (c.isAssignableFrom(n.getClass())) {
                    numbers[i]++;
                }
            }
        }
        return numbers;
    }
    /**
     * Retourne les éléments du réseau qui sont des instances des classes données en paramètre. Cette fonction n'est à utiliser
     * que si on veut des éléments de plusieurs classes, les alternatives getSubStations, getPowerPlants et getGroups sont optimisées
     * pour leurs cas d'utilisations.
     * @param classes les classes dont on veut les instances.
     * @return les éléments du réseau instances d'une classe de classes.
     * @see #getSubStation()
     * @see #getPowerPlants()
     * @see #getGroups()
     */
    public ArrayList<Node> getNodesInstancesOf(Class... classes){
        ArrayList<Node>  elements = new ArrayList<>();
        for (Node n : nodes){
            for (int i = 0; i < classes.length; i++){
                Class c = classes[i];
                if (c.isAssignableFrom(n.getClass())){
                    elements.add(n);
                }
            }
        }
        return elements;
    }
    /**
     * Retourne les sous-station du réseau. On évite de réutiliser getNodesInstancesOf et on préfère tirer avantage du fait que this.nodes est triée.
     * @return les éléments de nodes qui sont aussi des instances de SubStation.
     */
    public ArrayList<SubStation> getSubStations(){
        ArrayList<SubStation> stations = new ArrayList<>();
        int i = nodes.size()-1;
        Node n = nodes.get(i);
        while (i >= 0 && n instanceof SubStation){
            stations.add((SubStation)n);
            n = nodes.get(--i);
        }
        return stations;
    }
    /**
     * @return les centrales du réseau.
     */
    public ArrayList<PowerPlant> getPowerPlants(){
        ArrayList<PowerPlant> plants = new ArrayList<>();
        int i = 0;
        Node n = nodes.get(0);
        while (i < nodes.size() && n instanceof PowerPlant){
            plants.add((PowerPlant)n);
            n = nodes.get(++i);
        }
        return plants;
    }
    /**
     * @return les groupes du réseau.
     */
    public ArrayList<Group> getGroups(){
        // on pourrait certainement optimiser cette recherche-là aussi, à voir.
        // En attendant on se contente d'utiliser getNodesInstancesOf.
        ArrayList<Node> groupNodes = getNodesInstancesOf(Group.class);
        ArrayList<Group> groups = new ArrayList<>(groupNodes.size());
        for (Node n : groupNodes){
            groups.add((Group)n);
        }
        return groups;
    }
    /**
     * @return la production totale du réseau, correspondant à celle de toutes les centrales connectées. 
     */
    public int getProduction() {
        return production;
    }

    /**
     * @return la consommation totale du réseau, correspondant à celle de tous les groupes de consommation connectés. 
     */
    public int getConsumption() {
        return consumption;
    }

    /**
     * @return les éléments du réseau. 
     */
    public ArrayList<Node> getNodes() {
        return nodes.getArray();
    }
}