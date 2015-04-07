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
		return "Brand";
		
	}
	
	@RequestMapping(value = "search.do", method = {RequestMethod.GET,RequestMethod.POST})
	public String searchCars(String keyWords ,HttpServletRequest request,ModelMap map){
		List<Car> cars = carService.searchCars(keyWords);
		if(cars == null || cars.isEmpty()){
			//TODO
		}
		map.put("cars", cars);
		return "Brand";
		
	}

	@RequestMapping(value = "getCarDetail.do", method = {RequestMethod.GET,RequestMethod.POST})
	public String getCarDetail(int id ,ModelMap map){
		Car car = carService.getCarDetail(id);
		if(car == null){
			//TODO
		}
		map.put("car", car);
		return "CarDetail";

	}
}
