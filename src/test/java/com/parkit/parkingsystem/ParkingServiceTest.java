package com.parkit.parkingsystem;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ParkingServiceTest {
	
    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    
    private ParkingService parkingService;

    @BeforeEach
    private void setUpPerTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
	
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
        public void processExitingVehicleTest(){
    	
    	//GIVEN
    	when(ticketDAO.getNbTicket(anyString())).thenReturn(2);
    	
    	//WHEN
        parkingService.processExitingVehicle();
        
        //THEN
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    } 
    
    
    @Test
    public void testProcessIncomingVehicle(){
    
	//GIVEN
	when(inputReaderUtil.readSelection()).thenReturn(1);
	when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
	
	
	
	//WHEN
    parkingService.processIncomingVehicle();
    
    //THEN
    verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
}

    
@Test
public void processExitingVehicleTestUnableUpdate() {
   	
//GIVEN
when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false); 
   	
   	
//WHEN
parkingService.processExitingVehicle();
    
//THEN
verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
    
}     
    
  
    
 @Test
 public void testGetNextParkingNumberIfAvailable(){
	 when(inputReaderUtil.readSelection()).thenReturn(1);
	 when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
	
	 
	 parkingService.getNextParkingNumberIfAvailable();
	 
	 
	 verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(ParkingType.CAR);
	 
	 
 
 }
 
 
 @Test
 public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() throws Exception{
	 when(inputReaderUtil.readSelection()).thenReturn(1);
	 when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(0);
	
	 
	 parkingService.getNextParkingNumberIfAvailable();
	 
	 
	 verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(ParkingType.CAR);
	 
	 
 
 }
 
 
 @Test
 public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgum() throws Exception{
	 when(inputReaderUtil.readSelection()).thenReturn(3);
	 when(parkingSpotDAO.getNextAvailableSlot(null)).thenReturn(0);
	
	 
	 parkingService.getNextParkingNumberIfAvailable();
	 
	 
	 verify(inputReaderUtil, Mockito.times(1)).readSelection();
	 
	 
 
 }
 
 }
 

 
 
 
    

