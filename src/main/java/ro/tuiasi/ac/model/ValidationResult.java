package ro.tuiasi.ac.model;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
	private boolean valid;
	private List<String> errors;

	public ValidationResult(boolean valid) {
		this.valid = valid;
		this.errors = new ArrayList<>();
	}

	public void addError(String error) {
		this.valid = false;
		this.errors.add(error);
	}

	public boolean isValid() {
		return valid;
	}

	public List<String> getErrors() {
		return errors;
	}
}