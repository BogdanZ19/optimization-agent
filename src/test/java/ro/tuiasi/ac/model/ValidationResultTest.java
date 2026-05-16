package ro.tuiasi.ac.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Teste pentru ValidationResult")
class ValidationResultTest {

	@Test
	@DisplayName("Ar trebui să fie valid la creare, fără nicio eroare")
	void shouldBeValidInitially() {
		// WHEN
		ValidationResult result = new ValidationResult(true);

		// THEN
		assertThat(result.isValid()).isTrue();
		assertThat(result.getErrors()).isEmpty();
	}

	@Test
	@DisplayName("Ar trebui să devină invalid automat când se adaugă o eroare")
	void shouldBecomeInvalidWhenErrorIsAdded() {
		// GIVEN
		ValidationResult result = new ValidationResult(true);

		// WHEN
		result.addError("O eroare de sintaxă a apărut");

		// THEN
		assertThat(result.isValid()).isFalse();
		assertThat(result.getErrors()).hasSize(1).containsExactly("O eroare de sintaxă a apărut");
	}
}