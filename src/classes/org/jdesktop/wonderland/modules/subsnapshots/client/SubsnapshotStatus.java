/**
 * Open Wonderland
 *
 * Copyright (c) 2011 - 2012, Open Wonderland Foundation, All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * The Open Wonderland Foundation designates this particular file as
 * subject to the "Classpath" exception as provided by the Open Wonderland
 * Foundation in the License file that accompanied this code.
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
