package com.example.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.model.KeluargaModel;

@Mapper
public interface KeluargaMapper
{
	@Select("SELECT * FROM keluarga WHERE id=#{id}")
	KeluargaModel selectKeluargaById(@Param("id") int id);
	
	@Select("SELECT * FROM keluarga WHERE nomor_kk=#{nkk}")
	KeluargaModel selectKeluarga(@Param("nkk") String nkk);
	
	@Insert("INSERT INTO keluarga (alamat, nomor_kk, rt, rw, id_kelurahan, is_tidak_berlaku)"
    		+ " VALUES ('${alamat}', '${nomor_kk}', '${rt}', '${rw}', ${id_kelurahan}, 0)")
    void addKeluarga (KeluargaModel keluarga);
	
	@Update("UPDATE keluarga SET alamat = #{alamat}, nomor_kk = #{nomor_kk}, rt = #{rt}, rw = #{rw}, "
			+ "id_kelurahan = #{id_kelurahan}, is_tidak_berlaku = #{is_tidak_berlaku} "
			+ "WHERE id = #{id}")
    void updateKeluarga (KeluargaModel keluarga);
	
	@Update("UPDATE keluarga SET is_tidak_berlaku = 1 where id = #{id}")
	void setTidakBerlaku(@Param("id") String id);
}
