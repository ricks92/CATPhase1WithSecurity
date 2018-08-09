package com.hsc.cat.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hsc.cat.entity.EmployeeSkillEntity;

@Repository
public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkillEntity, Integer>{

	//@Query("select e from EmployeeSkillEntity e where e.empId:=empId AND e.skillId:=skillId AND e.weekNumber:=weekNumber AND e.ratingDoneBy:=ratingDoneBy")
		EmployeeSkillEntity findByEmpIdAndSkillIdAndWeekNumberAndRatingDoneBy(String empId,int skillId,int weekNumber,String ratingDoneBy);
		
		
		
		List<EmployeeSkillEntity> findByEmpId(String empId);
		
		@Query("select e from EmployeeSkillEntity e WHERE e.empId=:empId and weekNumber BETWEEN :start and :end")
		List<EmployeeSkillEntity> findByEmpIdCustom(@Param("empId")String empId, @Param("start")int start, @Param("end")int end);
		
		
//		@Query("select e from EmployeeSkillEntity e where e.empId:=empId and e.skillId:=skillId and e.ratingDoneBy:=ratingDoneBy order by e.weekNumber asc")
		List<EmployeeSkillEntity> findByEmpIdAndSkillIdAndRatingDoneByOrderByWeekNumber(@Param("empId")String empId, @Param("skillId")int skillId, @Param("ratingDoneBy")String ratingDoneBy);
		
		@Modifying(clearAutomatically = true)
		@Transactional
		@Query("UPDATE EmployeeSkillEntity e SET  e.rating=:rating,e.comment=:comment,e.creationDate=:creationDate WHERE e.skillId=:skillId AND e.weekNumber=:weekNumber AND e.ratingDoneBy=:ratingDoneBy ") 
		int updateEmployeeSkill(@Param("skillId") int skillId,@Param("rating")String rating,@Param("comment")String comment,@Param("weekNumber") int weekNumber,@Param("ratingDoneBy") String ratingDoneBy,@Param("creationDate") Date creationDate);
		
		@Query("select e from EmployeeSkillEntity e WHERE e.empId=:empId")
		List<EmployeeSkillEntity> findByEmpIdSkill(@Param("empId")String empId);
}
