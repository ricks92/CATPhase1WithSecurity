package com.hsc.cat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hsc.cat.entity.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer>{

	Skill findBySkillName(String skillName);
	@Query("select skills from Skill skills where skills.skillName LIKE :skillName%")
	List<Skill> findSkillsByName(@Param("skillName")String skillName);
	
	
	//List<Skill> findBySkillNameContaining(String skillName);
}
