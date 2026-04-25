package ro.tuiasi.ac.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptimizationControllerTest {

    @Test
    void testConnectionReturnsExpectedMessage() {
        OptimizationController controller = new OptimizationController();

        String result = controller.testConnection();

        assertEquals("Spring Controller works!", result);
    }
}