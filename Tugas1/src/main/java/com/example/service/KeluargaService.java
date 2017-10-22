package com.example.service;

import com.example.model.KeluargaModel;

public interface KeluargaService
{
	KeluargaModel selectKeluargaById(int id);
	
	KeluargaModel selectKeluarga(String nkk);
	
	void addKeluarga(KeluargaModel keluarga);
	
	void updateKeluarga(KeluargaModel keluarga);
}
