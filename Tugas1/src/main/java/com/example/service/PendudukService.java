package com.example.service;

import java.util.List;

import com.example.model.PendudukModel;

public interface PendudukService
{
	PendudukModel selectPenduduk(String nik);
	
	List<PendudukModel> selectPendudukByKeluarga(int id_keluarga);
	
	void addPenduduk(PendudukModel penduduk);
	
	void updatePenduduk(PendudukModel penduduk);
	
	void setWafatPenduduk(PendudukModel penduduk);
	
	List<PendudukModel> selectPendudukByIdKelurahan(String id_kelurahan);
	
	void generateNik(PendudukModel penduduk);
}
