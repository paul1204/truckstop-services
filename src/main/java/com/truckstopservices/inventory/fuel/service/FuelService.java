package com.truckstopservices.inventory.fuel.service;

import com.truckstopservices.inventory.fuel.dto.FuelInventoryResponse;
import com.truckstopservices.inventory.fuel.entity.Diesel;
import com.truckstopservices.inventory.fuel.entity.MidGradeOctane;
import com.truckstopservices.inventory.fuel.entity.PremiumOctane;
import com.truckstopservices.inventory.fuel.entity.RegularOctane;
import com.truckstopservices.inventory.fuel.model.FuelModel;
import com.truckstopservices.inventory.fuel.repository.DieselRepository;
//import com.truckstopservices.inventory.fuel.repository.FuelRepository;
import com.truckstopservices.inventory.fuel.repository.MidGradeFuelRepository;
import com.truckstopservices.inventory.fuel.repository.PremimumFuelRepository;
import com.truckstopservices.inventory.fuel.repository.RegularFuelRepository;
import com.truckstopservices.processing.dto.ShiftReportDto;
import com.truckstopservices.processing.entity.ShiftReport;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<FuelInventoryResponse> fuelInventoryList = new ArrayList<>();
        fuelInventoryList.addAll(
                dieselRepository.findAll().stream()
                .map(diesel -> new FuelInventoryResponse("Diesel",diesel.getTotalGallons()))
                .toList());
        fuelInventoryList.addAll(
                regularFuelRepository.findAll().stream()
                .map(regularOctane -> new FuelInventoryResponse("87",regularOctane.getTotalGallons()))
                .toList());
        fuelInventoryList.addAll(
                midGradeFuelRepository.findAll().stream()
                .map(midGradeOctane -> new FuelInventoryResponse("89",midGradeOctane.getTotalGallons()))
                .toList());
        fuelInventoryList.addAll(
                premimumFuelRepository.findAll().stream()
                .map(premiumOctane -> new FuelInventoryResponse("93",premiumOctane.getTotalGallons()))
                .toList());

        return fuelInventoryList;
    }

    //Add Fuel To Inventory

    //Update Fuel Inventory
    public void updateFuelInventoryDeductAvailableGallons(ShiftReportDto shiftReportDto){
        RegularOctane regularOctane = regularFuelRepository.findByOctane(87)
                .orElseThrow(()-> new EntityNotFoundException("Regular Not Found"));
        regularOctane.updateGallonsReduceInventory(shiftReportDto.fuelSaleRegular());

        MidGradeOctane midGrade = midGradeFuelRepository.findByOctane(89)
                .orElseThrow(()-> new EntityNotFoundException("Mid Grade Not Found"));
        midGrade.updateGallonsReduceInventory(shiftReportDto.fuelSalesMidGrade());

        PremiumOctane premiumOctane = premimumFuelRepository.findByOctane(91)
                .orElseThrow(()-> new EntityNotFoundException("Premium Grade Not Found"));
        premiumOctane.updateGallonsReduceInventory(shiftReportDto.fuelSalesPremium());

        Diesel diesel = dieselRepository.findByOctane(40)
                .orElseThrow(()-> new EntityNotFoundException("Diesel Not Found"));
        diesel.updateGallonsReduceInventory(shiftReportDto.fuelSalesDiesel());
    }

    public void updateRegularFuelInventory(double regularOctane){

        //regularFuelRepository.save(sRDto.fuelSaleRegular());
        //regularFuelRepository.save()
    }

}
