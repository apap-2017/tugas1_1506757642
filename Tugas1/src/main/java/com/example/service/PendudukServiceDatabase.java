package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.KeluargaMapper;
import com.example.dao.PendudukMapper;
import com.example.model.KeluargaModel;
import com.example.model.PendudukModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PendudukServiceDatabase implements PendudukService {
	@Autowired
	private PendudukMapper pendudukMapper;
	
	@Autowired
	private KeluargaMapper keluargaMapper;

	@Override
	public PendudukModel selectPenduduk(String nik) {
		log.info("select penduduk with nik {}", nik);
		return pendudukMapper.selectPenduduk(nik);
	}

	@Override
	public List<PendudukModel> selectPendudukByKeluarga(int id_keluarga) {
		log.info("select anggota keluarga with id_keluarga{}", id_keluarga);
		return pendudukMapper.selectPendudukByKeluarga(id_keluarga);
	}

	@Override
	public void addPenduduk(PendudukModel penduduk) {
		log.info("add penduduk with nik {}", penduduk.getNik());
		pendudukMapper.addPenduduk(penduduk);
	}
	
	@Override
	public void updatePenduduk(PendudukModel penduduk) {
		log.info("update penduduk with id {}", penduduk.getId());
		pendudukMapper.updatePenduduk(penduduk);
	}

	@Override
	public void setWafatPenduduk(PendudukModel penduduk) {
		
		pendudukMapper.setWafatPenduduk(penduduk.getNik());
		
		int id_keluarga = penduduk.getId_keluarga();
		
		List<PendudukModel> anggotaKeluarga = pendudukMapper.selectPendudukByKeluarga(id_keluarga);
		
		
		int count = 0;
				
		for(int i = 0; i < anggotaKeluarga.size(); i++) {
			int temp = Integer.parseInt((anggotaKeluarga.get(i).getIs_wafat()));
			if(temp == 1) {
				count++;
			}
		}
		
		if (count == anggotaKeluarga.size()) {
			keluargaMapper.setTidakBerlaku(id_keluarga + "");
		}
	}

	@Override
	public List<PendudukModel> selectPendudukByIdKelurahan(String id_kelurahan) {
		return pendudukMapper.selectPendudukByIdKelurahan(id_kelurahan);
	}
	
	@Override
	public void generateNik(PendudukModel penduduk) {
		String[] split = penduduk.getTanggal_lahir().split("-");
		String tahun = split[0];
		String tahunLahir = tahun.substring(2);
		String bulanLahir = split[1];
		String tanggalLahir = split[2];
		
		KeluargaModel keluarga = keluargaMapper.selectKeluargaById(penduduk.getId_keluarga());
		
		int gender = Integer.parseInt(penduduk.getJenis_kelamin());
		
		if(gender == 1) {
			int temp = Integer.parseInt(tanggalLahir);
			temp += 40;
			tanggalLahir = temp + "";
		}
		
		long nik = Long.parseLong(keluarga.getNomor_kk().substring(0, 6) + tanggalLahir + bulanLahir + tahunLahir + "0001");
		
		while (true) {
			PendudukModel checker = pendudukMapper.selectPenduduk("" + nik);
			if (checker != null) {
				nik++;
			} else {
				break;
			}
		}
		
		penduduk.setNik("" + nik);
	}
}
