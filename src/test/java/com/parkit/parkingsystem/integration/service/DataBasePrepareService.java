package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.NoSuchElementException;

public class DataBasePrepareService {

    DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    public void clearDataBaseEntries(){
        Connection connection = null;
        try{
            connection = dataBaseTestConfig.getConnection();

            //set parking entries to available
            connection.prepareStatement("update parking set available = true").execute();

            //clear ticket entries;
            connection.prepareStatement("truncate table ticket").execute();

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }
    
    public boolean checkIfPriceAndHourExitNotNull(String parkingNumber) {
        try(Connection connection = dataBaseTestConfig.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(DBConstants.GET_COUNT_NOT_NULL);
            ps.setString(1, parkingNumber);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
            	return rs.getInt(1) > 0;
            }
            else {
            	throw new NoSuchElementException("soucis ticket introuvable");
            	
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	return false;
        }
    }


}
