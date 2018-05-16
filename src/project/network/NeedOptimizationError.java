/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.network;

import java.util.ArrayList;

/**
 *
 * @author jimenezc
 */
public class NeedOptimizationError extends NetworkError{
    private SubStation station;
    public NeedOptimizationError(SubStation station){
        super();
        this.station = station;
    }
    
    public SubStation getStation(){
        return this.station;
    }
    
    @Override
    public void solve(){
        int powerNeeded = this.station.getPowerIn();
        ArrayList<PowerPlant> plants = new ArrayList<>();
        for (Line l : this.station.getLines()){
            PowerPlant plant = l.getIn();
            if (plant.getState() == PowerPlant.State.ON){
                plants.add(plant);
                this.station.giveBackPower(l, l.getActivePower());
            }
        }
        plants.sort(PowerPlant.powerComparator);
        for (PowerPlant p : plants){
            int powerAsked = (powerNeeded <= Math.abs(p.getActivePower()))?powerNeeded:Math.abs(p.getActivePower());
            powerNeeded -= p.grantToStation(station, powerAsked);
        }
        this.setSolved(true); // optimisme
    }
}
