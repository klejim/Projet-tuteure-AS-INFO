package project.network;

import java.io.FileNotFoundException;
import project.util.ConfigParser;
import project.util.SortedArrayList;
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
    private HashMap<String,HashMap<String,Object>> config; // contenu du fichier config
    private int production, consumption;
    private SortedArrayList<Node> nodes;
    private Network() throws FileNotFoundException {
        nodes = new SortedArrayList<>(Node.comparator);
        config = ConfigParser.parse("config");
        System.out.print(ConfigParser.stringify(config));
        String networkFile = (String)config.get("DEFAULT").get("network");
        //initNetwork(ConfigParser.parse(networkFile)); // non fonctionnel pour l'instant
        TESTInitNetwork();
    }
    /**
     * Constructeur. Bidon pour l'instant, l'initialisation du réseau se fait dans {#TESTInitNetwork TESTInitNetwork}.
     * @param nStations non utilisé.
     * @param nPowerPlants non utilisé.
     * @param nGroups  non utilisé.
     * @throws java.io.FileNotFoundException une erreur a eu lieu lors de la lecture des fichiers de configuration.
     */
    // TODO : un vrai constructeur
    public Network(int nStations, int nPowerPlants, int nGroups) throws FileNotFoundException{
        this();
    }
    /**
     * Initialisation du réseau à partir de la valeur de config.
     * @param network un tableau associatif représentant le réseau.
     * @see ConfigParser
     */
    // TODO : terminer le chargement du réseau
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
     * Analyse le réseau à la recherche d'erreurs.
     * @return la liste des erreurs rencontrées.
     */
    public ArrayList<NetworkError> analyze(){
        ArrayList<NetworkError> errors = new ArrayList<>();
        for (Node n : nodes){
            if (!n.isConnected()){
                errors.add(new NodeNotConnectedError(n));
            }
            if (n instanceof SubStation){
                SubStation station = (SubStation)n;
                int diff = station.getDiff();
                if (diff > 0) {
                    errors.add(new TooMuchPowerError(station, station.getDiff()));
                } else if (diff < 0) {
                    errors.add(new NotEnoughPowerError(station, Math.abs(station.getDiff())));
                }
            }
        }
        return errors;
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
    public ArrayList<NetworkError> handleErrors(ArrayList<NetworkError> rawErrors){
        ArrayList<NetworkError> errors = new ArrayList<>(rawErrors.size());
        errors.addAll(rawErrors);
        for (NetworkError e : rawErrors){
            if (e instanceof NotEnoughPowerError){
                NotEnoughPowerError err = (NotEnoughPowerError) e;
                if (solvePowerShortage(err.getStation(), err.getPower())){
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
    /**
     * Tente de résoudre un manque de puissance au niveau d'une sous-station.
     * <p>On effectue la recherche sur les centrales liées à la station et ordonnée par état (ON &lt; STARTING &lt; OFF) et par puissance
     * disponible croissante.</p>
     * <p>Ordre de recherche de solutions:</p>
     * <ul>
     * <li>on regarde si les centrales allumées ont encore de la puissance de disponible</li>
     * <li>si ce n'est pas le cas on voit s'il existe une centrale en démarrage qui pourrait aider</li>
     * <li>s'il n'y en a pas on essaie d'allumer une centrale</li>
     * <li>si c'est impossible on panique et on retourne false</li>
     * </ul>
     * <p>L'ordre de recherche est assuré par le tri précédent. On emprunte en priorité aux centrales ayant le moins de puissance disponible.
     *  Si une centrale ne peut pas combler toute l'erreur on prend ce qu'elle propose.</p>
     * @param station la sous-station concernée par l'erreur.
     * @param p la puissance qu'il manque pour alimenter correctement la sous-station.
     * @return {@code true} si l'erreur a pu être corrigée et {@code false} sinon.
     */
    private boolean solvePowerShortage(SubStation station, int p){
        boolean ok = false;
        ArrayList<PowerPlant> plants = new ArrayList<>();
        for (Line line : station.getLines()){
            plants.add(line.getIn());
        }
        // avant de poursuivre on trie les centrales
        // ordre : ON < STARTING < OFF et à catégorie égale la centrale ayant la plus grande puissance disponible est placée avant
        plants.sort(PowerPlant.stateAndPowerComparator);
        // Recherche de solution
        int i = 0, powerNeeded = p;
        while (i < plants.size() && !ok){
            PowerPlant plant = plants.get(i++);
            if (Math.abs(plant.getActivePower()) > 0){
                if (plant.getState() == PowerPlant.State.OFF){
                    plant.start();
                }
                int powerAsked = (powerNeeded <= Math.abs(plant.getActivePower()))?powerNeeded:Math.abs(plant.getActivePower());
                powerNeeded -= plant.grantToStation(station, powerAsked);
                ok = powerNeeded <= 0;
            }
        }
        return ok;
    }
    /**
     * Tente de résoudre une surcharge au niveau d'une sous-station en libèrant de la puissance.
     * <p>On travaille sur les lignes transmettant le moins de puissance en premier afin de privilégier les centrales les plus souples 
     * (hydrauliques plutôt que nucléaires) et de libérer autant de centrales que possible.</p>
     * @param station la sous-station concernée par l'erreur.
     * @param overflow la puissance en trop.
     * @return {@code true} si l'erreur a été corrigée et {@code false} sinon.
     */
    private boolean solvePowerOverflow(SubStation station, int overflow){
        boolean ok = false;
        ArrayList<Line> lines = new ArrayList<>();
        lines.addAll(station.getLines());
        lines.sort(Line.powerComparator);
        int i = 0, powerOverflow = overflow;
        while (i < lines.size() && !ok){
            Line line = lines.get(i++);
            PowerPlant plant = line.getIn();
            int tmp = (powerOverflow > line.getActivePower())?line.getActivePower():powerOverflow;
            powerOverflow -= station.giveBackPower(line, tmp);
            // on éteint ensuite la centrale si elle n'est plus utile
            if (plant.getActivePower() == plant.getPower()){
                plant.stop();
            }
            ok = (powerOverflow == 0);
        }
        return ok;
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
     * @return la liste d'erreur générées par {@link #handleErrors(java.util.ArrayList) handleErrors()}. 
     */
    public void update(){
        // TODO : mise à jour consommation des groupes
        // remarque : on suppose que la puissance de base d'une centrale est constante. Si cela change dans le futur il faudra modiier
        // la gestion des centrales et de leur puissance disponible.
        // nodes étant trié le parcours suivant met à jour tous les groupes, puis toutes centrales et enfin les sous-stations
        consumption = 0;
        production = 0;
        for (Node node : nodes){
            node.update();
            if (node instanceof SubStation){
                consumption += ((SubStation) node).getPowerOut();
                production += ((SubStation) node).getPowerIn();
            }
        }
    }
    
    
    /**
     * Initialise un réseau relativement simple.
     */
    private void TESTInitNetwork(){
        NuclearPlant np = new NuclearPlant("nucléaire");
        HydraulicPlant hp1 = new HydraulicPlant("hydraulique 1"), hp2 = new HydraulicPlant("hydraulique 2");
        GasPlant gp1 = new GasPlant("gaz 1");
        SubStation s1 = new SubStation("station 1"), s2 = new SubStation("station 2"), s3 = new SubStation("station 3");
        Group g1 = new Group(500000, "groupe 1"), g2 = new Group(300000, "groupe 2"), g3 = new Group(200000,"groupe 3");
        Group g4 = new Group(100000, "groupe 4"), g5 = new Group(200000, "groupe 5"), g6 = new Group(250000, "groupe 6");
        Group g7 = new Group(250000, "groupe 7"), g8 = new Group(250000, "groupe 8");
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
        return nodes.getArray();
    }
}