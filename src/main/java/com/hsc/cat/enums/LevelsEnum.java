package com.hsc.cat.enums;

public enum LevelsEnum {

	
	 NOVICE(1,"Novice"),
	 BEGINNER(2,"Beginner"),
	 PRACTITIONER(3,"Practitioner"),
	 COACH(4,"Coach"),
	 LEADER(5,"Leader"),
	 CANNOT_ASSESS(0,"Cannot Assess");
	
	
	int level;
	String levelName;
	
	private LevelsEnum(int a,String b) {
		level=a;
		levelName=b;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	
	public static int getLevelFromName(String levelName) {
		LevelsEnum[] array=LevelsEnum.values();
		int level=0;
		for(int i=0;i<array.length;i++) {
			if(array[i].getLevelName().equalsIgnoreCase(levelName)) {
				level=array[i].getLevel();
			}
		}
		
		return level;
	}
	
	
	
	public static String getLevelNameFromLevel(int level) {
		LevelsEnum[] array=LevelsEnum.values();
		String levelName="";
		for(int i=0;i<array.length;i++) {
			
			if(array[i].getLevel()==level) {
				
				levelName=array[i].getLevelName();
			}
		}
		
		
		return levelName;
	}
}
