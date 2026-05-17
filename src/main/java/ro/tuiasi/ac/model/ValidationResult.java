package ro.tuiasi.ac.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result of a validation operation.
 * <p>
 * Contains a flag showing whether validation succeeded and a list of
 * error messages describing validation failures.
 */
public class ValidationResult {
	private boolean valid;
	private List<String> errors;

	/**
	 * Creates a new validation result.
	 *
	 * @param valid true if the validated entity is valid, false otherwise
	 */
	public ValidationResult(boolean valid) {
		this.valid = valid;
		this.errors = new ArrayList<>();
	}

	/**
	 * Adds an error message and marks the result as invalid.
	 *
	 * @param error the validation error message to add
	 */
	public void addError(String error) {
		this.valid = false;
		this.errors.add(error);
	}

	/**
	 * Returns whether the validation result is valid.
	 *
	 * @return true if validation passed, false otherwise
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Returns the list of validation error messages.
	 *
	 * @return the list of errors
	 */
	public List<String> getErrors() {
		return errors;
	}
}