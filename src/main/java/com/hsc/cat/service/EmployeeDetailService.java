package com.hsc.cat.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hsc.cat.TO.EmployeeTO;
import com.hsc.cat.TO.EmployeeUnderManagerDetails;
import com.hsc.cat.TO.ManagerDetails;
import com.hsc.cat.TO.ResponseTO;
import com.hsc.cat.TO.ViewTeamTO;
import com.hsc.cat.VO.EmployeeDetailsVO;
import com.hsc.cat.VO.UpdateSkillVO;
import com.hsc.cat.email.MailSender;
import com.hsc.cat.entity.EmployeeDetails;
import com.hsc.cat.entity.EmployeeSkillEntity;
import com.hsc.cat.entity.UserDetailsEntity;
import com.hsc.cat.enums.ApprovalStatusEnum;
import com.hsc.cat.repository.EmployeeDetailRepository;
import com.hsc.cat.repository.EmployeeSkillRepository;
import com.hsc.cat.repository.UserRepository;
import com.hsc.cat.utilities.JSONOutputEnum;
import com.hsc.cat.utilities.Roles;

@Service
public class EmployeeDetailService {

	@Autowired
	private EmployeeDetailRepository employeeDetailRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
    @Qualifier("javasampleapproachMailSender")
	public MailSender mailSender;
	
	public EmployeeTO save(EmployeeDetailsVO evo) {
		EmployeeTO employeeTO = new EmployeeTO();
		
	      boolean userAlreadyExists=   employeeDetailRepository.exists(evo.getUsername());
	      employeeTO.setIssue("Username already exists");
	      if(userAlreadyExists) return null;
	      
	      EmployeeDetails emailAlreadyExists=employeeDetailRepository.getEmail(evo.getEmail());
	      employeeTO.setIssue("Email already exists");
	      if(emailAlreadyExists!=null) return null;
	      
	      
		EmployeeDetails emp= new EmployeeDetails();
		UserDetailsEntity user = new UserDetailsEntity();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
			Date d1 = new Date();
			Date d2 = new Date();
			emp.setCreationDate(d1);
			System.out.println(emp.getCreationDate());
			emp.setUpdationDate(d2);
			
			
		
		
		//Setting users
		user.setUsername(evo.getUsername());
		user.setPassword(encoder.encode(evo.getPassword()));
		user.setRole(evo.getRole());
		
		//Setting EmployeeDetails
		EmployeeDetails saved=null;
		
		
		//emp.setEmpid(evo.getUsername());
		emp.setFirstName(evo.getFirstName());
		emp.setLastName(evo.getLastName());
		emp.setDepartment(evo.getDepartment());
		emp.setSecurityQues1(evo.getSecurityQues1());
		emp.setSecurityQues2(evo.getSecurityQues2());
		emp.setSecurityAns1(encoder.encode(evo.getSecurityAns1()));
		emp.setSecurityAns2(encoder.encode(evo.getSecurityAns2()));
		emp.setManagerId(evo.getManagerId());
		emp.setEmail(evo.getEmail());
		emp.setEmpid(evo.getUsername());
		
		if(evo.getRole().equals("ROLE_MANAGER")){ //That is it is a manager
			System.out.println("Manager wants to register");
			emp.setApprovalStatus(ApprovalStatusEnum.PENDING.getStatus());
			
			
			
			String from = "undermyumbrella247@gmail.com";
			String to = "knwnobounds@gmail.com";
			String subject = "Request came to register manager!";
			String body = "Request came to register manager with employee id:"+evo.getUsername()+"\nDetails: \nEmployee id: "+evo.getUsername()+"\nFirst Name: "+evo.getFirstName()+"\nLast Name: "+evo.getLastName()+"\nEmail: "+evo.getEmail()+"\nPlease verify: http://10.11.26.22:8060/cat/verifyManager/"+evo.getUsername();
			
			mailSender.sendMail(from, to, subject, body); //send email
			  user.setEmployeeDetails(emp);  //save manager
			  System.out.println("Manager saved");
			 userRepository.save(user);
			 System.out.println("Login details saved");
			 saved = employeeDetailRepository.save(emp);
			
		}
		
//		else{
//			user.setEmployeeDetails(emp);
//			 userRepository.save(user);
//			 saved = employeeDetailRepository.save(emp);
//			emp.setApprovalStatus(ApprovalStatusEnum.NA.getStatus());
//		}
		
		if(evo.getRole().equals("ROLE"+Roles.EMPLOYEE) && emp.getManagerId()!=null) {  //Check if it is a valid manager id
			if(!employeeDetailRepository.exists(emp.getManagerId())) {
				System.out.println("Manger details incorrect");
				//do nothing
				employeeTO.setIssue("Manger details incorrect");
			}
			else if(employeeDetailRepository.exists(emp.getManagerId())) {
				emp.setApprovalStatus(ApprovalStatusEnum.NA.getStatus());
				user.setEmployeeDetails(emp);
				 userRepository.save(user);
				 saved = employeeDetailRepository.save(emp);
				 user.setEmployeeDetails(emp);
				 userRepository.save(user);
				 saved = employeeDetailRepository.save(emp);
				
				}
		}
//		else {
//		 user.setEmployeeDetails(emp);
//		 userRepository.save(user);
//		 saved = employeeDetailRepository.save(emp);
//		
//		}
//	
		 if (saved != null)
				employeeTO = modelConversion(saved);
		
		return employeeTO;
	}
	
	
	
	
	public List<EmployeeTO> getAllEmployees(){
		List<EmployeeDetails> employees= employeeDetailRepository.findAllEmployees();
		
		
		List<EmployeeTO> employeeTOList = new ArrayList<>();
		for(EmployeeDetails emp:employees) {
			EmployeeTO employeeTO=modelConversion(emp);
			employeeTOList.add(employeeTO);
		}
		
		return employeeTOList;
	}
	
	@Scheduled(cron="0 0 0 * * ?")
	public ResponseTO deleteManagers() {
		List<EmployeeDetails> employeesToDelete=employeeDetailRepository.findAllManagersToDelete();
		ResponseTO response = new ResponseTO();
		if(employeesToDelete.isEmpty()) {
			response.setResponseCode(String.valueOf(JSONOutputEnum.SUCCESS.getValue()));
			response.setResponseMessage("No manager is there to purge");
		}
		
		else {
			for(EmployeeDetails e:employeesToDelete) {
				String email=e.getEmail();
				String from = "undermyumbrella247@gmail.com";
				String to = email;
				String subject = "Your request has been rejected!";
				String body =  "Your request has been rejected!";
				
				mailSender.sendMail(from, to, subject, body); //send email
				
				employeeDetailRepository.delete(e);
			}
		}
		
		return response;
	}
	
	
	
	
	public List<ManagerDetails> getAllManager() {

		List<ManagerDetails> managerDetailsList = new ArrayList<ManagerDetails>();
		List<EmployeeDetails> employeeList = employeeDetailRepository.findAllManagers();
		for (EmployeeDetails employeeDetails : employeeList) {
			ManagerDetails managerDetails = new ManagerDetails();
			managerDetails.setEmpId(employeeDetails.getEmpid());
			managerDetails.setFirstName(employeeDetails.getFirstName());
			managerDetails.setLastName(employeeDetails.getLastName());
			managerDetails.setEmailId(employeeDetails.getEmail());
			managerDetailsList.add(managerDetails);
		}

		return managerDetailsList;
	}
	
	
	
	public boolean updateApprovalStatus(String empId) {
		boolean result = Boolean.FALSE;
		 String approvalStatus=employeeDetailRepository.getApprovalStatus(empId);
		int updatdRow = employeeDetailRepository.updateManagersApprovalStatus(approvalStatus, empId);
		if (updatdRow > 0) {
			result = Boolean.TRUE;
		}

		return result;
	}

	
	
	public ViewTeamTO getEmployeeUnderManager(String managerId) {

		ViewTeamTO viewTeamTO = new ViewTeamTO();
		if (null != managerId) {
			List<EmployeeDetails> employeeDetailsList = employeeDetailRepository.findEmployeeUnderManager(managerId);
			if (employeeDetailsList != null) {
				List<EmployeeUnderManagerDetails> listOfEmployee = new ArrayList<EmployeeUnderManagerDetails>();
				for (EmployeeDetails employeeDetails : employeeDetailsList) {
					EmployeeUnderManagerDetails employeeUnderManagerDetails = new EmployeeUnderManagerDetails();
					employeeUnderManagerDetails.setEmpId(employeeDetails.getEmpid());
					employeeUnderManagerDetails.setFirstName(employeeDetails.getFirstName());
					employeeUnderManagerDetails.setLastName(employeeDetails.getLastName());
					employeeUnderManagerDetails.setDepartment(employeeDetails.getDepartment());
					employeeUnderManagerDetails.setEmailId(employeeDetails.getEmail());
					listOfEmployee.add(employeeUnderManagerDetails);
				}
				if (listOfEmployee != null) {
					viewTeamTO.setListOfEmployee(listOfEmployee);
					viewTeamTO.setResponseCode("1");
					viewTeamTO.setResponseMessage("SUCCESS");
				} else {
					viewTeamTO.setResponseCode("8");
					viewTeamTO.setResponseMessage("No employee exists against this managerId");
				}
			} else {
				viewTeamTO.setResponseCode("8");
				viewTeamTO.setResponseMessage("No employee exists against this managerId");

			}
		} else {
			viewTeamTO.setResponseCode("5");
			viewTeamTO.setResponseMessage("Invalid Parameter");
		}

		return viewTeamTO;

	}
	
	public EmployeeTO modelConversion(EmployeeDetails e){
		EmployeeTO employeeTO = new EmployeeTO();
		employeeTO.setEmpid(e.getEmpid());
		employeeTO.setFirstName(e.getFirstName());
		employeeTO.setLastName(e.getLastName());
		employeeTO.setDepartment(e.getDepartment());
		employeeTO.setCreationDate(e.getCreationDate());
		employeeTO.setUpdationDate(e.getUpdationDate());
		employeeTO.setSecurityQues1(e.getSecurityQues1());
		employeeTO.setSecurityQues2(e.getSecurityQues2());
		employeeTO.setSecurityAns1(e.getSecurityAns1());
		employeeTO.setSecurityAns2(e.getSecurityAns2());
		employeeTO.setManagerId(e.getManagerId());
		employeeTO.setApprovalStatus(e.getApprovalStatus());
		employeeTO.setEmail(e.getEmail());
		return employeeTO;
	}
}
