package com.pomelo.car.web.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.pomelo.car.web.model.Car;

@Component
public class CarDao extends BaseDao<Car> {
	public  CarDao() {
		super.setType(Car.class);
	}
	public List<Car> findListByBrandId(String brandId){
		String sql ="SELECT c.id AS id, c.`name` AS `name`, c.icon AS icon, c.brandId AS brandId, c.detail AS detail FROM Car_CarInfo AS c WHERE c.brandId=?";
		return getList(sql, brandId);
	}
}
