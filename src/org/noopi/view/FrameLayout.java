package org.noopi.view;

import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.noopi.utils.listeners.AddRuleEventListener;
import org.noopi.utils.listeners.NewFileEventListener;
import org.noopi.utils.listeners.OpenFileEventListener;
import org.noopi.utils.listeners.RemoveRuleEventListener;
import org.noopi.utils.listeners.RunEventListener;
import org.noopi.utils.listeners.SaveEventListener;
import org.noopi.utils.listeners.SpeedChangeEventListener;
import org.noopi.utils.listeners.StepEventListener;
import org.noopi.utils.listeners.StopEventListener;
import org.noopi.utils.listeners.TapeInitializationEventListener;

public class FrameLayout implements IFrameLayout {

    //ATTRIBUTS
    private JPanel mainPanel;
    private JMenuBar menuBar;

    @Override
    public JComponent getView() {
        return mainPanel;
    }

    @Override
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    @Override
    public void shiftTapeRight() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void shiftTapeLeft() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setSymbolOnTape() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setMachineState() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addRule() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeRule() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resetRules() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pushHistory() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void popHistory() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addAddRuleEventListener(AddRuleEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addRemoveRuleEventListener(RemoveRuleEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addTapeInitializationEventListener(TapeInitializationEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addStepEventListener(StepEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addRunEventListener(RunEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addStopEventListener(StopEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addSpeedChangeEventListener(SpeedChangeEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addOpenFileEventListener(OpenFileEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addNewFileEventListener(NewFileEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addSaveEventListener(SaveEventListener l) {
        // TODO Auto-generated method stub
        
    }
    
}
