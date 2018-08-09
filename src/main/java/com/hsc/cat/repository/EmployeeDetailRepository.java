package com.hsc.cat.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hsc.cat.entity.EmployeeDetails;

@Repository
public interface EmployeeDetailRepository extends JpaRepository<EmployeeDetails, String>{
	
	@Query("SELECT e FROM EmployeeDetails e WHERE approvalStatus='APPROVED'") 
    List<EmployeeDetails> findAllManagers();
	
	
	@Modifying
	@Transactional
	@Query("UPDATE EmployeeDetails e SET  e.approvalStatus=:approvalStatus WHERE e.empid=:empid") 
    int updateManagersApprovalStatus(@Param("approvalStatus") String approvalStatus,@Param("empid") String empid);
	
	@Query("select e from EmployeeDetails e where  e.managerId=:managerId order by firstName asc")
	 List<EmployeeDetails> findEmployeeUnderManager(@Param("managerId") String managerId);
	
	
	@Query("SELECT e FROM EmployeeDetails e WHERE approvalStatus='NA'") 
    List<EmployeeDetails> findAllEmployees();
	
	
	@Query("SELECT e FROM EmployeeDetails e WHERE approvalStatus='REJECTED'") 
    List<EmployeeDetails> findAllManagersToDelete();
	
	@Query("select e.approvalStatus from EmployeeDetails e WHERE e.empid=:empid")
	String getApprovalStatus(@Param("empid")String empid);
	
	@Query("select e from EmployeeDetails e WHERE e.empid=:empid")
	EmployeeDetails getEmail(@Param("empid")String empid);
}
