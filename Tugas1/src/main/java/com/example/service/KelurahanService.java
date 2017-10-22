package com.example.service;

import java.util.List;

import com.example.model.KelurahanModel;

public interface KelurahanService
{
	KelurahanModel selectKelurahanById(int id);
	
	List<KelurahanModel> selectAllKelurahan();
	
	List<KelurahanModel> selectKelurahanByIdKecamatan(String id_kecamatan);
}
