package com.example.service;

import java.util.List;

import com.example.model.KecamatanModel;

public interface KecamatanService
{
	KecamatanModel selectKecamatanById(int id);
	
	List<KecamatanModel> selectAllKecamatan();
	
	List<KecamatanModel> selectKecamatanByIdKota(String id_kota);
}
