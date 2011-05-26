/**
 * (c) 2003 Jérôme Nègre
 * 
 */

package org.jnegre.foghorn;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Jérôme Nègre (plugin@jnegre.org)
 *
 * (c) Copyright 2003 Jérôme Nègre - http://www.jnegre.org/
 * 
 */
public class ActivateFoghornAction implements IObjectActionDelegate {

    IWorkbenchPart targetPart;

    /**
     * Constructor for ActivateFoghornAction.
     */
    public ActivateFoghornAction() {
        super();
    }

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.targetPart = targetPart;
    }

    /**
     * @see IActionDelegate#run(IAction)
     */
    public void run(IAction action) {
        IProject project =
            (IProject)((IAdaptable) ((StructuredSelection) targetPart.getSite().getSelectionProvider().getSelection())
                .getFirstElement()).getAdapter(IProject.class);
        try {
            IProjectDescription desc = project.getDescription();
            ICommand[] commands = desc.getBuildSpec();
            boolean found = false;

            for (int i = 0; i < commands.length; ++i) {
                if (commands[i].getBuilderName().equals("org.jnegre.foghorn.builder")) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                //add builder to project
                ICommand command = desc.newCommand();
                command.setBuilderName("org.jnegre.foghorn.builder");
                ICommand[] newCommands = new ICommand[commands.length + 1];

                // Add it after other builders.
                System.arraycopy(commands, 0, newCommands, 0, commands.length);
                newCommands[commands.length] = command;
                desc.setBuildSpec(newCommands);
                project.setDescription(desc, null);
            }
        } catch (CoreException e) {
            Shell shell = new Shell();
            MessageDialog.openInformation(shell, "Foghorn", "Error: " + e);
        }
    }

    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
    }

}
