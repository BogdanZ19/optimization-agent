package ro.tuiasi.ac.agent;

public class AgentStep {
	private int iteration; // Numărul iterației (1, 2, 3...)
	private String action; // "PROPOSE", "VALIDATE"
	private String codeOutput; // Codul generat în acest pas specific
	private String status; // "SUCCESS" sau "FAILED"
	private String message; // Erorile de validare dacă există

	public AgentStep(int iteration, String action, String codeOutput, String status, String message) {
		this.iteration = iteration;
		this.action = action;
		this.codeOutput = codeOutput;
		this.status = status;
		this.message = message;
	}

	public int getIteration() {return iteration;}
	public void setIteration(int iteration) {this.iteration = iteration;}

	public String getAction() {return action;}
	public void setAction(String action) {this.action = action;}

	public String getCodeOutput() {return codeOutput;}
	public void setCodeOutput(String codeOutput) {this.codeOutput = codeOutput;}

	public String getStatus() {return status;}
	public void setStatus(String status) {this.status = status;}

	public String getMessage() {return message;}
	public void setMessage(String message) {this.message = message;}

}
