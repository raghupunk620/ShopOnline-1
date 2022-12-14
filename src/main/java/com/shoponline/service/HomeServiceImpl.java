package com.shoponline.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoponline.model.ShippingAddress;
import com.shoponline.model.User;

@Service
public class HomeServiceImpl implements HomeService 
{
	private Logger logger = LoggerFactory.getLogger(HomeServiceImpl.class.getName());
	
	@Autowired
	UserService userService;

	@SuppressWarnings("unchecked")
	@Override
	public void saveUserAddress(ShippingAddress shippingAddress, String userName) {
		
		logger.info("save user : "+userName+" address to DB");
		User user = userService.findUserByUserName(userName);
		
		try {
			JSONObject json = new JSONObject();

			json.put("name", shippingAddress.getName());
			json.put("mobileNumber", shippingAddress.getMobileNumber());
			json.put("pinCode", shippingAddress.getPinCode());
			json.put("address", shippingAddress.getAddress());
			json.put("city", shippingAddress.getCity());
			json.put("state", shippingAddress.getState());
			
			user.setUserAddress(json.toString());	
			userService.saveUser(user);	
			
		} catch (Exception ex) {
			logger.error("Exception occurred while saving address. Error : " + ex.getMessage());
		}
	  
	}

	@Override
	public ShippingAddress getUserShippingAddress(String userName){
		logger.info("getting address from JSON");
		User user = userService.findUserByUserName(userName);

		JSONParser parser = new JSONParser();
		ShippingAddress shippingAddress = new ShippingAddress();

		try {
			JSONObject json = (JSONObject) parser.parse(user.getUserAddress());

			shippingAddress.setName(json.get("name").toString());
			shippingAddress.setMobileNumber(json.get("mobileNumber").toString());
			shippingAddress.setPinCode(json.get("pinCode").toString());
			shippingAddress.setAddress(json.get("address").toString());
			shippingAddress.setCity(json.get("city").toString());
			shippingAddress.setState(json.get("state").toString());

		} catch (ParseException ex) {
			logger.error("Exception occurred while parsing. Error : " + ex.getMessage());
		}
		return shippingAddress;
	}
	
	
	
}
