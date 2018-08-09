package com.hsc.cat.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hsc.cat.TO.SkillTO;
import com.hsc.cat.VO.AddSkillVO;
import com.hsc.cat.entity.Skill;
import com.hsc.cat.repository.SkillRepository;



@Service
public class SkillService {

	@Autowired
	private SkillRepository skillRepository;
	
	public SkillTO addSkill(AddSkillVO svo) {
		
		Skill recordExists=skillRepository.findBySkillName(svo.getSkillName());
		
		if(recordExists!=null) {
			SkillTO skillTO = new SkillTO();
			skillTO.setIssue("Record already exists");
			return skillTO;
		}
		Skill skill = new Skill();
		skill.setSkillName(svo.getSkillName());
		skill.setDescription(svo.getDescription());
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		Date d1 = new Date();
		Date d2 = new Date();
		
		skill.setCreationDate(d1);
		skill.setUpdationDate(d2);
		
		Skill saved=skillRepository.save(skill);
		SkillTO skillTO=null;
		
		if(saved!=null) {
			skillTO=modelConversion(skill);
		}
			
		return skillTO;
	}
	
	
	//Fetch all skills in the database
	public List<SkillTO> fetchAllSkills() {
		List<Skill> skills=skillRepository.findAll();
		List<SkillTO> skillTOList=new ArrayList<>();
		for(Skill s:skills) {
			SkillTO skillTO=modelConversion(s);
			skillTOList.add(skillTO);
		}
			
		return skillTOList;
	}
	
	
	
	public SkillTO modelConversion(Skill skill) {
		SkillTO skillTO = new SkillTO();
		skillTO.setSkillId(skill.getSkillId());
		skillTO.setSkillName(skill.getSkillName());
		skillTO.setDescription(skill.getDescription());
		skillTO.setSkillId(skill.getSkillId());
		skillTO.setCreationDate(skill.getCreationDate());
		skillTO.setUpdationDate(skill.getUpdationDate());
		
		return skillTO;
	}
}
