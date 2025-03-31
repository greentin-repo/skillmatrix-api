package com.greentin.enovation.security;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.greentin.enovation.model.EmployeeDetails;


@Repository
public interface UserRepository extends JpaRepository<EmployeeDetails, Integer> {
	List<EmployeeDetails> findByEmpIdIn(List<Integer> userIds);
	EmployeeDetails findByEmailId(String emailId);
	//@Query(value = "SELECT * FROM USERS WHERE EMAIL_ADDRESS = ?1", nativeQuery = true)
	@Query("SELECT e FROM EmployeeDetails e where (e.cmpyEmpId= :emailId or e.contactNo = :emailId or e.emailId = :emailId ) and e.isDeactive=0 and e.empType not in ('CW')")
	EmployeeDetails findByContactNoOrEmailIdOrCmpyEmpId(@Param("emailId") String emailId);

}