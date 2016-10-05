package ve.com.mastercircuito.components;

public class DispatchPlace {
	
	private Integer id;
	private String place;
	
	public DispatchPlace() {
		
	}
	
	public DispatchPlace(Integer id, String place) {
		super();
		this.id = id;
		this.place = place;
	}
	
	public String getPlace() {
		return this.place;
	}
	
	public void setPlace(String place) {
		this.place = place;
	}
	
	public Integer getId() {
		return this.id;
	}
	
}