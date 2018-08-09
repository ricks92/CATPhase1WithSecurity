package com.hsc.cat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hsc.cat.TO.SkillTO;
import com.hsc.cat.VO.AddSkillVO;
import com.hsc.cat.service.SkillService;
import com.hsc.cat.utilities.JSONOutputEnum;
import com.hsc.cat.utilities.JSONOutputModel;
import com.hsc.cat.utilities.RESTURLConstants;
import com.hsc.cat.utilities.Roles;


@RestController
@CrossOrigin
//@RequestMapping("/cat")
public class SkillController {

	
	@Autowired
	private SkillService skillService;
	@ResponseBody
	@RequestMapping(value=RESTURLConstants.SKILLS,method=RequestMethod.POST)
	@PreAuthorize("hasAnyRole('"+Roles.MANAGER+"')")
	public JSONOutputModel addSkill(@RequestBody AddSkillVO svo) {
		JSONOutputModel output = new JSONOutputModel();
		SkillTO skillTO=skillService.addSkill(svo);
		if(skillTO!=null) {
			
			if(skillTO.getIssue()!=null) {
				output.setData(new String(skillTO.getIssue()));
				output.setStatus(JSONOutputEnum.FAILURE.getValue());
				output.setMessage("Duplicate entry is not allowed!!");
			}
			else {
			output.setData(skillTO);
			output.setStatus(HttpStatus.CREATED.value());
			output.setMessage("Skills saved successfully");
			}
			
		}
		else {
			output.setData(skillTO);
			output.setStatus(JSONOutputEnum.FAILURE.getValue());
			output.setMessage("Skills could not be saved");
		}
		
		
		return output;
	}
	
	
	@ResponseBody
	@RequestMapping(value=RESTURLConstants.SKILLS,method=RequestMethod.GET)
	@PreAuthorize("hasAnyRole('"+Roles.EMPLOYEE+"','"+Roles.MANAGER+"')")
	//@PreAuthorize("hasAnyRole('"+Roles.SUPER_ADMIN+"','"+Roles.GROUP_ADMIN+"','"+Roles.EMPLOYEE+"')")
	public JSONOutputModel fetchAllSkills() {
		JSONOutputModel output = new JSONOutputModel();
		
		List<SkillTO> skillTOList=skillService.fetchAllSkills();
		if(!skillTOList.isEmpty() && skillTOList.size()!=0) {
			output.setData(skillTOList);
			output.setStatus(JSONOutputEnum.SUCCESS.getValue());
			output.setMessage("Skills fetched successfully");
		}else {
			output.setData(skillTOList);
			output.setStatus(JSONOutputEnum.FAILURE.getValue());
			output.setMessage("No skill found");
		}
		
		return output;
	}
}
