package com.example.service;

import java.util.List;

import com.example.model.KotaModel;

public interface KotaService
{
	KotaModel selectKotaById(int id);
	
	List<KotaModel> selectAllKota();
}
