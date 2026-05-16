package ro.tuiasi.ac.agent;

import java.util.ArrayList;
import java.util.List;

public class AgentState {
    private String originalCode;          // Codul citit inițial din fișier [cite: 120]
    private String finalCode;
    private List<AgentStep> steps;        // Istoricul complet al loop-ului [cite: 25, 128]
    private boolean isFinalized;          // Dacă am ajuns la un rezultat valid [cite: 127]
    private String lastErrors;            // Ultimele erori pentru promptul de revizuire 

    public AgentState(String originalCode) {
        this.originalCode = originalCode;
        this.steps = new ArrayList<>();
        this.isFinalized = false;
    }

    

    public String getOriginalCode() { return originalCode; }
    public void setOriginalCode(String originalCode) { this.originalCode = originalCode; }

    public String getFinalCode() { return finalCode; }
    public void setFinalCode(String finalCode) { this.finalCode = finalCode; }

    public List<AgentStep> getSteps() { return steps; }
    
    public void addStep(AgentStep step) { this.steps.add(step); }

    public boolean isFinalized() { return isFinalized; }
    public void setFinalized(boolean finalized) { isFinalized = finalized; }

    public String getLastErrors() { return lastErrors; }
    public void setLastErrors(String lastErrors) { this.lastErrors = lastErrors; }
}
