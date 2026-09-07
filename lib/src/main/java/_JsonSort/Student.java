package _JsonSort;


public class Student {
	
	private String no;
	private String name;
	private String kurasu; // JSONのキー名と一致させます
    private int age;
    private int val;

    // ゲッター・セッター
    public String getNo() {
    	return no; 
    }
    public void setNo(String no) {
    	this.no = no; 
    }
    public String getName() { 
    	return name; 
    }
    public void setName(String name) { 
    	this.name = name; 
    }
    public String getKurasu() { 
    	return kurasu;  
    }
    public void setKurasu(String kurasu) { 
    	this.kurasu = kurasu; 
    }
    public int getAge() {
    	return age; 
    }
    public void setAge(int age) { 
    	this.age = age; 
    }
    public int getVal() {
    	return val; 
    }
    public void setVal(int val) { 
    	this.val = val; 
    }
}

