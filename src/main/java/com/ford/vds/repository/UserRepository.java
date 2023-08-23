package com.ford.vds.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ford.vds.domain.User;

public interface UserRepository extends JpaRepository<User, String> {
	
	@Query(value = "SELECT * FROM  KVDSM02_USERMASTER WHERE VDSM02_EMPLOYEE_ID IN (:empID) ", nativeQuery = true)
	Optional<User> findByEmployeeId(@Param("empID") String empID);
	
	@Query(value = "SELECT VDSM03_VALUE_X FROM  KVDSM03_LOV WHERE VDSM03_CATEGORY_N IN (:access) ", nativeQuery = true)
	String findScreenAccess(@Param("access") String access);
	
}
