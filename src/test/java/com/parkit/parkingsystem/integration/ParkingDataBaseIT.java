package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
    	Mockito.lenient().when(inputReaderUtil.readSelection()).thenReturn(1);
        Mockito.lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar() throws Exception{ 	
    	
    	
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        
        parkingService.processIncomingVehicle();
        
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        
        assertNotNull(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
        
    }

    @Test
    public void testParkingLotExit() throws Exception{
    	
        testParkingACar();
        Thread.sleep(1000);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        
        //TODO: check that the fare generated and out time are populated correctly in the database
        
        
        assertEquals(dataBasePrepareService.checkIfPriceAndHourExitNotNull("ABCDEF"), true);
        
    }
    
    @Test
    public void testParkingLotExitWithPrice() throws Exception{
    	
    
    	testParkingACar();
        dataBasePrepareService.checkIfPriceNotNullInDataBase(1);
        
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        //TODO: check that the fare generated and out time are populated correctly in the database
        
        //assertEquals(FareCalculatorService.calculateFare());
        //assertEquals(dataBasePrepareService.checkIfPriceIsGood(Fare.CAR_RATE_PER_HOUR), true);
        assertEquals(dataBasePrepareService.checkIfPriceAndHourExitNotNull("ABCDEF"), true);
        

        
    }
    
@Test
  public void testParkingLotExitRecurringUser() throws Exception {
	
 testParkingACar();
 testParkingLotExit();
 ParkingService parkingService =  new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
 parkingService.processIncomingVehicle();
 Thread.sleep(10000);
 parkingService.processExitingVehicle();
 
 //TODO: check that the fare generated and out time are populated correctly in the database
        
      
 assertEquals(dataBasePrepareService.checkIfPriceAndHourExitNotNull("ABCDEF"), true);       
 
    	
    } 

}
