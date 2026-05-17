package ro.tuiasi.ac.agent;

/**
 * Represents a single step performed by the optimization agent during the
 * analysis loop.
 *
 * <p>
 * Each step stores information about one optimization iteration, including the
 * iteration number, the action performed, the generated code output, the result
 * status, and an optional message such as validation errors or success details.
 * </p>
 */
public class AgentStep {
	private int iteration;
	private String action;
	private String codeOutput;
	private String status;
	private String message;

	/**
	 * Creates a new agent step record.
	 *
	 * @param iteration the iteration number of the optimization step
	 * @param action the action taken during the optimization step
	 * @param codeOutput the generated code output for this step
	 * @param status the result status of the step
	 * @param message an optional message, such as validation errors or details
	 */
	public AgentStep(int iteration, String action, String codeOutput, String status, String message) {
		this.iteration = iteration;
		this.action = action;
		this.codeOutput = codeOutput;
		this.status = status;
		this.message = message;
	}

	/**
	 * Returns the iteration number of this step.
	 *
	 * @return the iteration number
	 */
	public int getIteration() {return iteration;}

	/**
	 * Sets the iteration number for this step.
	 *
	 * @param iteration the iteration number to set
	 */
	public void setIteration(int iteration) {this.iteration = iteration;}

	/**
	 * Returns the action taken during this step.
	 *
	 * @return the action description
	 */
	public String getAction() {return action;}

	/**
	 * Sets the action taken during this step.
	 *
	 * @param action the action description to set
	 */
	public void setAction(String action) {this.action = action;}

	/**
	 * Returns the generated code output for this step.
	 *
	 * @return the generated code output
	 */
	public String getCodeOutput() {return codeOutput;}

	/**
	 * Sets the generated code output for this step.
	 *
	 * @param codeOutput the generated code output to set
	 */
	public void setCodeOutput(String codeOutput) {this.codeOutput = codeOutput;}

	/**
	 * Returns the result status for this step.
	 *
	 * @return the result status
	 */
	public String getStatus() {return status;}

	/**
	 * Sets the result status for this step.
	 *
	 * @param status the result status to set
	 */
	public void setStatus(String status) {this.status = status;}

	/**
	 * Returns the optional message for this step.
	 *
	 * @return the optional message
	 */
	public String getMessage() {return message;}

	/**
	 * Sets the optional message for this step.
	 *
	 * @param message the optional message to set
	 */
	public void setMessage(String message) {this.message = message;}

}
