package com.hsc.cat.utilities;

public class RESTURLConstants {

	public static final String BASE_URL="cat";
	public static final String REGISTER_USER=BASE_URL+"/auth/register";
	//public static final String REGISTER = BASE_URL+"/auth/register";
	public static final String USER_DELETION = BASE_URL+"/auth/user";
	public static final String PASSWORD_RESET = BASE_URL+"/user";
	public static final String LOGIN = BASE_URL+"/auth/login";
	public static final String LOGOUT = BASE_URL+"/auth/logout";
	public static final String SKILLS=BASE_URL+"/skills";
	public static final String UPDATE_SKILL=BASE_URL+"/updateSkill";
	public static final String VIEW_SKILL=BASE_URL+"/viewSkill/{empId}";
	public static final String FETCH_ALL_EMPLOYEES=BASE_URL+"/manager/employees";
	public static final String GET_MANAGER_DETAILS=BASE_URL+"/getManagerDetails";
	public static final String VERIFY_MANAGER=BASE_URL+"/verifyManager";
	public static final String VIEW_TEAM=BASE_URL+"/viewTeam/{id}";
	public static final String FETCH_MAP=BASE_URL+"/fetchMap";
	
	
}
