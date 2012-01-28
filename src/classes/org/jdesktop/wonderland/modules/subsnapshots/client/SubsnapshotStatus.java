/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdesktop.wonderland.modules.subsnapshots.client;

import javax.swing.SwingUtilities;
import org.jdesktop.wonderland.client.jme.JmeClientMain;

/**
 *
 * @author jkaplan
 */
public enum SubsnapshotStatus {
    INSTANCE;
    
    private SubsnapshotStatusFrame frame;
    
    public void startJob(final String name) {
        onAWTThread(new Runnable() {
            public void run() {
                if (frame == null) {
                    frame = new SubsnapshotStatusFrame();
                    frame.pack();
                    frame.setLocationRelativeTo(JmeClientMain.getFrame().getFrame());
                }
                
                frame.startJob(name);
                frame.setVisible(true);
            }
        });
    }
    
    public void statusUpdate(final String status) {
        onAWTThread(new Runnable() {
            public void run() {
                if (frame != null) {
                    frame.setStatus(status);
                }
            }
        });
    }
    
    public void endJob() {
        onAWTThread(new Runnable() {
           public void run() {
               if (frame != null) {
                   frame.endJob();
                   frame.dispose();
                   frame = null;
               }
           } 
        });
    }
    
    protected void onAWTThread(Runnable runnable) {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }
}
