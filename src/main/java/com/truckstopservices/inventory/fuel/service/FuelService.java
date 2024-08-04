package com.truckstopservices.inventory.fuel.service;

import com.truckstopservices.inventory.fuel.model.FuelModel;
import com.truckstopservices.inventory.fuel.repository.DieselRepository;
//import com.truckstopservices.inventory.fuel.repository.FuelRepository;
import com.truckstopservices.inventory.fuel.repository.MidGradeFuelRepository;
import com.truckstopservices.inventory.fuel.repository.PremimumFuelRepository;
import com.truckstopservices.inventory.fuel.repository.RegularFuelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FuelService {

    @Autowired
    private DieselRepository dieselRepository;

    @Autowired
    private RegularFuelRepository regularFuelRepository;

    @Autowired
    private MidGradeFuelRepository midGradeFuelRepository;

    @Autowired
    private PremimumFuelRepository premimumFuelRepository;

    public FuelService(DieselRepository dieselRepository, RegularFuelRepository regularFuelRepository, MidGradeFuelRepository midGradeFuelRepository, PremimumFuelRepository premimumFuelRepository) {
        this.dieselRepository = dieselRepository;
        this.regularFuelRepository = regularFuelRepository;
        this.midGradeFuelRepository = midGradeFuelRepository;
        this.premimumFuelRepository = premimumFuelRepository;
    }

    public List<?> getAllFuelInventory(){
        List<FuelModel> fuelInventoryList = new ArrayList<>();
        fuelInventoryList.addAll(dieselRepository.findAll());
        fuelInventoryList.addAll(regularFuelRepository.findAll());
        fuelInventoryList.addAll(midGradeFuelRepository.findAll());
        fuelInventoryList.addAll(premimumFuelRepository.findAll());

        return fuelInventoryList;
    }

    //Add Fuel To Inventory

    //Update Fuel Inventory
}
