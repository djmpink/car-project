package com.pomelo.car.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pomelo.car.web.dao.CarDao;
import com.pomelo.car.web.model.Car;
@Service(value = "carService")
public class CarService {
	@Autowired
	private CarDao carDao;
	
	public List<Car> findCarList(String brandId){
		return carDao.findListByBrandId(brandId);
	}
	
	public List<Car> searchCars(String keyWords){
		return carDao.findCarsBySearchName(keyWords);
	}

	public void setCarDao(CarDao carDao) {
		this.carDao = carDao;
	}
	
	
}
