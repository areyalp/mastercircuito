package ve.com.mastercircuito.objects;

public class BudgetStage {
	
	private Integer id;
	private String stage;
	
	public BudgetStage() {
		super();
	}
	
	public BudgetStage(Integer id, String stage) {
		super();
		this.id = id;
		this.stage = stage;
	}
	
	public BudgetStage(BudgetStage budgetStageInfo) {
		this.id = budgetStageInfo.getId();
		this.stage = budgetStageInfo.getStage();
	}

	public String getStage() {
		return this.stage;
	}
	
	public void setStage(String stage) {
		this.stage = stage;
	}
	
	public Integer getId() {
		return this.id;
	}
	
}
