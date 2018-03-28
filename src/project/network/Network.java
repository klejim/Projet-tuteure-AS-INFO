package project.network;

import java.util.ArrayList;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Classe représentant le réseau de distribution.
 * 
 * Elle s'occupe de la gestion du réseau (création, réaction aux évolutions,...) et sert de dialogue avec l'extérieur. 
 * @author Jimenez
 */
public class Network {
    static final int SIMULATION_PERIOD = 24;
    
    private HashMap<String,HashMap<String,Object>> config; // contenu du fichier config.txt
    private int production, consumption;
    private ArrayList<Node> nodes;
    private Network() throws IOException {
        nodes = new ArrayList<>();
        config = ConfigParser.parse("network");
        System.out.print(ConfigParser.stringify(config));
        String networkFile = (String)config.get("DEFAULT").get("network");
        initNetwork(ConfigParser.parse(networkFile));
        TESTInitNetwork();
    }
    /**
     * Constructeur. Bidon pour l'instant, l'initialisation du réseau se fait dans {#TESTInitNetwork TESTInitNetwork}.
     * @param nStations non utilisé
     * @param nPowerPlants non utilisé
     * @param nGroups  non utilisé
     * @throws java.io.IOException une erreur s'est produite lors de la lecture des fichiers de configuration
     */
    // todo : un vrai constructeur
    
    public Network(int nStations, int nPowerPlants, int nGroups) throws IOException{ // l'exception indique le contrôleur qu'une erreur critique a eu lieu lors de la création du réseau
        this();
    }
    /**
     * Initialisation du réseau à partir de la valeur de config
     */
    private void initNetwork(HashMap<String,HashMap<String,Object>> network){
        for(String key : network.keySet()){
            if(key.matches("GROUP_.+")){
                HashMap<String, Object> group = network.get(key);
                if(group.containsKey("name") && group.containsKey("consumption")){
                    String name = (String)group.get("name");
                    int consumption = Integer.parseInt((String)group.get("consumption"));
                    nodes.add(new Group(consumption, name));
                }
                else{
                }
            }
        }
    }
    /**
     * Analyse le réseau à la recherche d'erreurs
     * @return la liste des erreurs rencontrées
     */
    public ArrayList<NetworkError> analyze(){
        ArrayList<NetworkError> errors = new ArrayList<>();
        for (Node n : nodes){
            if (!n.isConnected()){
                errors.add(new NodeNotConnectedError(n));
            }
            if (n instanceof SubStation){
                // todo : ajuster seuils
                SubStation station = (SubStation)n;
                if (station.getDiff() > 0){ // demande supérieure à l'entrée
                    errors.add(new TooMuchPowerError(station, station.getDiff()));
                }
                else if (station.getDiff() < 0){
                    errors.add(new NotEnoughPowerError(station, Math.abs(station.getDiff())));
                }
            }
        }
        return errors;
    }
    private ArrayList<NetworkError> handleErrors(ArrayList<NetworkError> rawErrors){
        ArrayList<NetworkError> errors = new ArrayList<>(rawErrors.size());
        for (NetworkError e : rawErrors){
            if (e instanceof NotEnoughPowerError){
                NotEnoughPowerError err = (NotEnoughPowerError) e;
                if (solve1(err.getStation(), err.getPower())){
                    e.setSolved(true);
                }
                else{
                    e.setMessage("Correction automatique impossible");
                    CannotFindSolutionError newError = new CannotFindSolutionError(e);
                    errors.add(newError);
                }
            }
            else if (e instanceof TooMuchPowerError){
                TooMuchPowerError err = (TooMuchPowerError) e;
                if (solvePowerOverflow(err.getStation(), err.getPower())){
                    e.setSolved(true);
                }
                else{
                    e.setMessage("Correction automatique impossible");
                    CannotFindSolutionError newError = new CannotFindSolutionError(e);
                    errors.add(newError);
                }
            }
            else if (e instanceof NodeNotConnectedError){
                e.setMessage("Connexion manuelle nécessaire");
            }
        }
        return errors;
    }
    private boolean solve1(SubStation station, int p){
        return true;
    }
    private boolean solvePowerOverflow(SubStation station, int overflow){
        ArrayList<PowerPlant> tmp = new ArrayList<>();
        for (Line line : station.getLines()){
            PowerPlant plant = line.getIn();
            
        }
        return true;
    }
    
    public ArrayList<NetworkError> runOnce(){
        // todo : mise à jour consommation
        // todo : mise à jour sous-stations
        return handleErrors(analyze());
    }
    
    
    /**
     * Initialise un réseau relativement simple.
     */
    private void TESTInitNetwork(){
        NuclearPlant np = new NuclearPlant("nucléaire");
        HydraulicPlant hp1 = new HydraulicPlant("hydraulique 1"), hp2 = new HydraulicPlant("hydraulique 2");
        GasPlant gp1 = new GasPlant("gaz 1");
        SubStation s1 = new SubStation("station 1"), s2 = new SubStation("station 2"), s3 = new SubStation("station 3");
        Group g1 = new Group(400000, "groupe 1"), g2 = new Group(250000, "groupe 2"), g3 = new Group(610000,"groupe 3");
        Group g4 = new Group(30000, "groupe 4"), g5 = new Group(200000, "groupe 5"), g6 = new Group(400000, "groupe 6");
        Group g7 = new Group(240000, "groupe 7"), g8 = new Group(250000, "groupe 8");
        Node tab[] = {np, hp1, hp2, s1, s2, s3, g1, g2, g3, g4, g5, g6, g7, gp1, g8};
        nodes.addAll(Arrays.asList(tab));
        addGroupsToStation(s1, g1, g2);
        addGroupsToStation(s2, g3, g4, g5);
        addGroupsToStation(s3, g6, g7);
        addPlantsToStation(s1, np, gp1);
        addPlantsToStation(s2, np, gp1, hp2);
        addPlantsToStation(s3, np, hp2);
        
    }
    /**
     * Regroupe les étapes nécessaires à l'ajout de groupes de consommation à une sous-station.
     * @param s la sous-station à laquelle ajouter les groupes.
     * @param gr les groupes de consommation à ajouter.
     */
    public void addGroupsToStation(SubStation s, Group... gr){
        for (Group g : gr){
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
    public void addPlantsToStation(SubStation s, PowerPlant... plants){
        for (PowerPlant p : plants){
            Line line = new Line(p, s);
            p.addLines(line);
            s.addLines(line);
            p.updateLines();
            p.updateStations();
        }
        computeProduction();
    }    
    /**
     * Met à jour la consommation totale du réseau en fonction des besoins de chaque sous-station.
     */
    public void computeConsumption(){
        consumption = 0;
        for (Node n : nodes){
            if (n instanceof SubStation){
                consumption += ((SubStation) n).getPowerOut();
            }
        }
    }
    /**
     * Met à jour la production totale du réseau à partir de la puissance reçue par chaque sous-station.
     */
    public void computeProduction(){
        production = 0;
        for (Node n : nodes){
            if (n instanceof SubStation){
                production += ((SubStation) n).getPowerIn();
            }
        }
    }
    /**
     * Compte le nombre d'instances des classes données en paramètre dans le tableau nodes.
     * @param classes les classes dont on veut compter les instances.
     * @return un tableau contenant le nombre d'instance de chaque classe.
     */
    public int[] count(Class... classes){
        int numbers[] = new int[classes.length];
        for (Node n : nodes){
            for (int i=0;i<classes.length;i++){
                Class c = classes[i];
                if (c.isAssignableFrom(n.getClass())){
                    numbers[i]++;
                }
            }
        }
        return numbers;
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
        return nodes;
    }
}
