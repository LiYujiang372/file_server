package com.demo.file_server.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.demo.file_server.dao.entity.FileStorage;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {
	
	@Query(value = "UPDATE wotianyu_file_storage" + 
			" SET save_size = ?2, finish = ?3" + 
			" WHERE id = ?1", nativeQuery = true)
	@Modifying
	@Transactional
	int update(long id, int save_size, int finish);
	
	@Query(value = "SELECT * FROM wotianyu_file_storage WHERE file_id = ?1", nativeQuery = true)
	FileStorage findByFileId(long fileId);

}
