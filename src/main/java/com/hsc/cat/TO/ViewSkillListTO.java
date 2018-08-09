package com.hsc.cat.TO;

import java.util.List;
import java.util.Set;

public class ViewSkillListTO {

	private List<ViewSkillTO> listOfEmployeeSkills;
	private Set<Integer> listOfSkillId;

	public Set<Integer> getListOfSkillId() {
		return listOfSkillId;
	}

	public void setListOfSkillId(Set<Integer> listOfSkillId) {
		this.listOfSkillId = listOfSkillId;
	}

	public List<ViewSkillTO> getListOfEmployeeSkills() {
		return listOfEmployeeSkills;
	}

	public void setListOfEmployeeSkills(List<ViewSkillTO> listOfEmployeeSkills) {
		this.listOfEmployeeSkills = listOfEmployeeSkills;
	}
	
	
}
