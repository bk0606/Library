package view.handlers;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  Realization of event handling.
 *
 *  @param handler - realization of IActionHandler,
 *  determines which handler use
 */
public class FrameActionListener implements ActionListener {
    private IActionHandler handler = null;
    /**
     * Implements main IActionHandler method - handleEvent(event)
     * Neded for realization different kinds of handling event,
     * like db queryes, or arrow clicks.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        handler.handleEvent(event);
    }
    /** Needed for create dialog windows on current frame */
    public static Frame getTopFrame() {
        Frame[] frames = Frame.getFrames();
        for(int i = 0; i < frames.length; i++) {
            if(frames[i].getFocusOwner() != null) {
                return frames[i];
            }
        }
        if(frames.length > 0) {
            return frames[0];
        }
        return null;
    }

    public FrameActionListener(IActionHandler handler) {
        this.handler = handler;
    }
}
