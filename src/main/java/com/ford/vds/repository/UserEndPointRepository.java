package com.ford.vds.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ford.vds.domain.UserEndpoints;


public interface UserEndPointRepository extends JpaRepository<UserEndpoints, String> {

	@Query(value = "SELECT * FROM USER_ENDPOINTS WHERE USER_ID IN (:address) ", nativeQuery = true)
	List<UserEndpoints> findConfirmAddress(@Param("address") String[] address);
	
	@Query(value = "SELECT * FROM  USER_ENDPOINTS WHERE ENDPOINT IN (:endPointKey) ", nativeQuery = true)
	List<UserEndpoints> findByEndPoint(@Param("endPointKey") String endPointKey);

}
