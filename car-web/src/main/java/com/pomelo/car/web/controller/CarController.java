package com.pomelo.car.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.pomelo.car.web.model.Car;
import com.pomelo.car.web.service.CarService;

@Controller
public class CarController {
	
	@Autowired
	private CarService carService;
	
	@RequestMapping(value = "getBrandList1.do", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView getBrandList1(String brandId){
		
		List<Car> cars = carService.findCarList(brandId);
		if(cars == null || cars.isEmpty()){
			//TODO
		}
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("Brand");
		modelAndView.addObject(cars);
		//System.out.println();
		return modelAndView;
		
	}
	
	@RequestMapping(value = "getBrandList.do", method = {RequestMethod.GET,RequestMethod.POST})
	public String getBrandList(String brandId ,HttpServletRequest request,ModelMap map){
		
		List<Car> cars = carService.findCarList(brandId);
		if(cars == null || cars.isEmpty()){
			//TODO
		}
		map.put("cars", cars);
		System.out.println("111111111"+cars.size());
		return "Brand";
		
	}
}
