# Java Code Optimization Agent

The **Optimization Agent** is an intelligent desktop application that leverages an AI agent (Gemini) to analyze and optimize Java source code. It utilizes a Spring Boot backend to manage the optimization loop and a Java Swing desktop interface for user interactions.

## 🚀 Features

* **AI-Powered Optimization Loop:** Uses the Gemini model to suggest code improvements. If a generated suggestion is invalid, the agent automatically revises its prompt with the validation errors and tries again (up to a configured maximum number of iterations).
* **Code Validation:** Built-in validation ensures that the suggested optimizations maintain code integrity before presenting them to the user.
* **Interactive Desktop GUI:** A split-screen Swing application allowing users to easily browse for a Java file, start the analysis, and visually compare the original code side-by-side with the optimized code.
* **One-Click Patching:** Users can "Accept" or "Reject" the AI's suggestions. Accepted optimizations are automatically applied (patched) directly to the source file.

## 🏗️ Architecture

The project is structured as a monolith combining a RESTful backend and a desktop frontend:

### Backend (Spring Boot)
* **Controllers:** `OptimizationController` handles analysis, acceptance, and rejection requests. `HealthController` provides readiness checks.
* **Agent Services:** `AgentLoopService` orchestrates the prompt building, AI generation (`GeminiClientService`), and validation (`CodeValidationService`).
* **Models:** Uses modern Java `record` types (`OptimizationRequest`, `OptimizationSuggestion`, `CodeFile`, etc.) to maintain immutable state across the application.

### Frontend (Java Swing)
* **UI Components:** `MainWindow` provides the visual layout, and `GuiEventListener` handles async background tasks via `SwingWorker` to keep the UI responsive.
* **API Client:** `BackendApiClient` manages HTTP communication (`localhost:8080`) with the Spring Boot REST endpoints.

## 📋 Prerequisites

* **Java 17 or higher** (due to the use of Java `record` types).
* **Maven** (or your preferred build tool wrapper).
* A valid **Gemini API key** configured via a `.env` file in the project root.

## 🛠️ How to Run

1. **Environment Setup:** Create a `.env` file in the root directory of the project and add your Gemini API key:

```env
GEMINI_API_KEY=your_api_key_here

```
2. **Start the Application:** Run the main class `CodeOptimizerApplication`. Note that the Spring Boot application is explicitly configured with `.headless(false)` to allow the Java Swing GUI to render.
3. **Use the GUI:**
* Click **Browse** to select a `.java` source file from your system.
* Click **START** to trigger the AI agent analysis.
* Review the suggested changes in the right-hand panel.
* Click **Accept** to apply the changes to the file, or **Reject** to discard them.

## 📂 Key Components

* `AgentLoopService.java`: The core AI optimization loop.
* `CodeOptimizerApplication.java`: The application entry point (bootstraps Spring and launches Swing).
* `BackendApiClient.java`: The bridge between the Swing UI and the Spring Boot API.
  """
