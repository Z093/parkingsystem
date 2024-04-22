package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
	

    public void calculateFare(Ticket ticket,boolean discount){ 
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());           
        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();
        //this.discount = discount;
        System.out.println(discount);
         double discountAmount = 1;
         if (discount) {
        	 discountAmount = 0.95;
         }
         
        //TODO: Some tests are failing here. Need to check if this logic is correct
        long duration = outHour - inHour;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
            	if( duration < (30 * 60 * 1000)) {
            		ticket.setPrice(0);	
            	}
            	else {
            		ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * discountAmount);
            	}
                break;
            }
            case BIKE: {
            	if( duration < (30 * 60 * 1000)) {
            		ticket.setPrice(0);	
            	}
            	else {
            		ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * discountAmount);
            	}

                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
    
    
    public void calculateFare(Ticket ticket){ 
        calculateFare(ticket, false); 
    }
    
}