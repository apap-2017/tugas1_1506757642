package com.example.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.service.KeluargaService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeluargaModel
{
	private int id;
	private String nomor_kk;
	private String alamat;
	private String rt;
	private String rw;
	private int id_kelurahan;
	private String is_tidak_berlaku;
	private String nama_kota;
	private String nama_kelurahan;
	private String nama_kecamatan;
	private String kode_kecamatan;
	
	public void generateNkk(KeluargaService keluargaDAO) {
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String[] tanggalSplit = date.split("-");
		String tahun = tanggalSplit[0].substring(2);
		String bulan = tanggalSplit[1];
		String tanggal = tanggalSplit[2];
		
		long nkk = Long.parseLong(this.getKode_kecamatan().substring(0,6) + tanggal + bulan + tahun + "0001");
		
		while(true) {
			KeluargaModel checker = keluargaDAO.selectKeluarga(""+nkk);
			if(checker != null) {
				nkk++;
			}else {
				break;
			}
		}
		this.setNomor_kk(""+nkk);
	}
}
