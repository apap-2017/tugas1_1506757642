package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.KecamatanModel;
import com.example.model.KeluargaModel;
import com.example.model.KelurahanModel;
import com.example.model.KotaModel;
import com.example.model.PendudukModel;
import com.example.service.KecamatanService;
import com.example.service.KeluargaService;
import com.example.service.KelurahanService;
import com.example.service.KotaService;
import com.example.service.PendudukService;


@Controller
public class PendudukController {
	@Autowired
	PendudukService pendudukDAO;

	@Autowired
	KeluargaService keluargaDAO;

	@Autowired
	KelurahanService kelurahanDAO;

	@Autowired
	KecamatanService kecamatanDAO;

	@Autowired
	KotaService kotaDAO;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/penduduk")
	public String viewPenduduk(@RequestParam(value = "nik", required = false) String nik, Model model) {
		PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);

		if (penduduk != null) {
			KeluargaModel keluarga = keluargaDAO.selectKeluargaById(penduduk.getId_keluarga());
			KelurahanModel kelurahan = kelurahanDAO.selectKelurahanById(keluarga.getId_kelurahan());
			KecamatanModel kecamatan = kecamatanDAO.selectKecamatanById(kelurahan.getId_kecamatan());
			KotaModel kota = kotaDAO.selectKotaById(kecamatan.getId_kota());

			model.addAttribute("penduduk", penduduk);
			model.addAttribute("keluarga", keluarga);
			model.addAttribute("kelurahan", kelurahan);
			model.addAttribute("kecamatan", kecamatan);
			model.addAttribute("kota", kota);

			return "view-penduduk";
		} else {
			model.addAttribute("nik", nik);
			return "not-found";
		}
	}

	@RequestMapping("/penduduk/tambah")
	public String addPenduduk(Model model) {
		PendudukModel penduduk = new PendudukModel();
		model.addAttribute("penduduk", penduduk);
		return "form-tambah-penduduk";
	}

	@RequestMapping(value = "/penduduk/tambah", method = RequestMethod.POST)
	public String addPendudukSubmit(Model model, @ModelAttribute PendudukModel penduduk) {
		pendudukDAO.generateNik(penduduk);
		pendudukDAO.addPenduduk(penduduk);
		
		model.addAttribute("nik", penduduk.getNik());
		return "sukses-tambah";
	}

	@RequestMapping("/penduduk/ubah/{nik}")
	public String ubahPenduduk(Model model, @PathVariable(value = "nik") String nik) {
		PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
		model.addAttribute("penduduk", penduduk);
		return "form-update-penduduk";
	}

	@RequestMapping(value = "/penduduk/ubah/{nik}", method = RequestMethod.POST)
	public String updatePenduduk(Model model, @PathVariable(value = "nik") String nik,
			@ModelAttribute PendudukModel penduduk) {
		PendudukModel pendudukLama = pendudukDAO.selectPenduduk(nik);
		
		model.addAttribute("nik", pendudukLama.getNik());
		
		penduduk.setId(pendudukLama.getId());
		
		if ((!pendudukLama.getTanggal_lahir().equals(penduduk.getTanggal_lahir())
			|| (pendudukLama.getId_keluarga() != penduduk.getId_keluarga()))
			|| (pendudukLama.getJenis_kelamin() != penduduk.getJenis_kelamin())	) {
			pendudukDAO.generateNik(penduduk);
			pendudukDAO.updatePenduduk(penduduk);
		}

		return "sukses-update";
	}

	@RequestMapping(value = "/penduduk/mati", method = RequestMethod.POST)
	public String pendudukMati(Model model, @RequestParam(value = "nik", required = false) String nik) {
		PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
		pendudukDAO.setWafatPenduduk(penduduk);
		
		model.addAttribute("nik", nik);
		return "sukses-set-wafat";
	}

	@RequestMapping("/keluarga")
	public String viewKeluarga(@RequestParam(value = "nkk", required = false) String nkk, Model model) {
		KeluargaModel keluarga = keluargaDAO.selectKeluarga(nkk);

		if (keluarga != null) {
			KelurahanModel kelurahan = kelurahanDAO.selectKelurahanById(keluarga.getId_kelurahan());
			KecamatanModel kecamatan = kecamatanDAO.selectKecamatanById(kelurahan.getId_kecamatan());
			KotaModel kota = kotaDAO.selectKotaById(kecamatan.getId_kota());
			List<PendudukModel> penduduk = pendudukDAO.selectPendudukByKeluarga(keluarga.getId());

			model.addAttribute("keluarga", keluarga);
			model.addAttribute("kelurahan", kelurahan);
			model.addAttribute("kecamatan", kecamatan);
			model.addAttribute("kota", kota);
			model.addAttribute("penduduk", penduduk);

			return "view-keluarga";
		} else {
			model.addAttribute("nkk", nkk);
			return "not-found";
		}
	}

	@RequestMapping("keluarga/tambah")
	public String addKeluarga(Model model) {
		KeluargaModel keluarga = new KeluargaModel();
		model.addAttribute("keluarga", keluarga);
		model.addAttribute("allKelurahan", kelurahanDAO.selectAllKelurahan());
		return "form-tambah-keluarga";
	}

	@RequestMapping(value = "/keluarga/tambah", method = RequestMethod.POST)
	public String tambahKeluarga(Model model, @ModelAttribute KeluargaModel keluarga) {
		keluarga.generateNkk(keluargaDAO);
		keluargaDAO.addKeluarga(keluarga);
		model.addAttribute("nkk", keluarga.getNomor_kk());
		return "sukses-tambah";
	}
	

	@RequestMapping("/keluarga/ubah/{nkk}")
	public String ubahKeluarga(Model model, @PathVariable(value = "nkk") String nkk) {
		KeluargaModel keluarga = keluargaDAO.selectKeluarga(nkk);
		model.addAttribute("allKelurahan", kelurahanDAO.selectAllKelurahan());
		model.addAttribute("keluarga", keluarga);
		return "form-update-keluarga";
	}

	@RequestMapping(value = "/keluarga/ubah/{nkk}", method = RequestMethod.POST)
	public String ubahKeluarga(Model model, @PathVariable(value = "nkk") String nkk,
			@ModelAttribute KeluargaModel keluarga) {
		
		KeluargaModel keluargaLama = keluargaDAO.selectKeluarga(nkk);
		keluarga.setNomor_kk(keluargaLama.getNomor_kk());
		keluarga.setId(keluargaLama.getId());
		keluarga.setIs_tidak_berlaku(keluargaLama.getIs_tidak_berlaku());

		if (keluargaLama.getId_kelurahan() != keluarga.getId_kelurahan()) {
			keluarga.generateNkk(keluargaDAO);
		}
		keluargaDAO.updateKeluarga(keluarga);
		
		List<PendudukModel> anggota = pendudukDAO.selectPendudukByKeluarga(keluargaLama.getId());
		for(int i = 0; i < anggota.size(); i++) {
			pendudukDAO.generateNik(anggota.get(i));
			pendudukDAO.updatePenduduk(anggota.get(i));
		}
		
		model.addAttribute("nkk", keluargaLama.getNomor_kk());
		
		return "sukses-update";
	}

	@RequestMapping(value = "/penduduk/cari")
	public String cariPendudukK(Model model, @RequestParam(value = "id_kota", required = false) String kt,
			@RequestParam(value = "id_kecamatan", required = false) String kc,
			@RequestParam(value = "id_kelurahan", required = false) String kl) {

		List<KotaModel> list_kota = kotaDAO.selectAllKota();
		model.addAttribute("list_kota", list_kota);
		model.addAttribute("id_kota", kt);
		String nama_kota = "";
		for(int i = 0; i < list_kota.size(); i++) {
			String temp = list_kota.get(i).getId() + "";
			if (temp.equals(kt)) {
				nama_kota = list_kota.get(i).getNama_kota();
			}
		}
		model.addAttribute("nama_kota", nama_kota);

		List<KecamatanModel> list_kecamatan = kecamatanDAO.selectKecamatanByIdKota(kt);
		model.addAttribute("list_kecamatan", list_kecamatan);
		model.addAttribute("id_kecamatan", kc);
		String nama_kecamatan = "";
		for(int i = 0; i < list_kecamatan.size(); i++) {
			String temp = list_kecamatan.get(i).getId() + "";
			if (temp.equals(kc)) {
				nama_kecamatan = list_kecamatan.get(i).getNama_kecamatan();
			}
		}
		model.addAttribute("nama_kecamatan", nama_kecamatan);

		List<KelurahanModel> list_kelurahan = kelurahanDAO.selectKelurahanByIdKecamatan(kc);
		model.addAttribute("list_kelurahan", list_kelurahan);
		model.addAttribute("id_kelurahan", kl);
		String nama_kelurahan = "";
		for(int i = 0; i < list_kelurahan.size(); i++) {
			String temp = list_kelurahan.get(i).getId() + "";
			if (temp.equals(kl)) {
				nama_kelurahan = list_kelurahan.get(i).getNama_kelurahan();
			}
		}
		model.addAttribute("nama_kelurahan", nama_kelurahan);
		
		if (kt != null && kc != null && kl != null) {
			List<PendudukModel> list_penduduk = pendudukDAO.selectPendudukByIdKelurahan(kl);
			model.addAttribute("penduduk", list_penduduk);
			
			return "list-penduduk";
		}
		
		return "cari-penduduk";
	}
}
