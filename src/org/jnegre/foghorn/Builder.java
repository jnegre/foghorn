/**
 * (c) 2003 Jérôme Nègre
 * 
 */

package org.jnegre.foghorn;

import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

/**
 * @author Jérôme Nègre (plugin@jnegre.org)
 *
 * (c) Copyright 2003 Jérôme Nègre - http://www.jnegre.org/
 * 
 */
public class Builder extends IncrementalProjectBuilder {


    /**
     * The constructor.
     */
    public Builder() {
    }

    /**
     * Insert the method's description here.
     * @see IncrementalProjectBuilder#build
     */
    protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
        try {
            IMarker markers[] = this.getProject().findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
            for(int i=0; i<markers.length; i++) {
                if(IMarker.SEVERITY_ERROR == markers[i].getAttribute(IMarker.SEVERITY,-1)) {
                    play("arrr.wav");
                    break;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static void play(String url) throws Exception {
        AudioInputStream stream = AudioSystem.getAudioInputStream(FoghornPlugin.getDefault().find(new Path("sound/"+url)));
        
        AudioFormat format = stream.getFormat();
        /*
        if(format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            format = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                format.getSampleRate(),
                format.getSampleSizeInBits() * 2,
                format.getChannels(),
                format.getFrameSize() * 2,
                format.getFrameRate(),
                true);//ouf...
            stream = AudioSystem.getAudioInputStream(format, stream);
        }
        */
        DataLine.Info info = new DataLine.Info(
            Clip.class,
            stream.getFormat(),
            ((int)stream.getFrameLength()*format.getFrameSize()));
        
        Clip clip = (Clip) AudioSystem.getLine(info);
        clip.open(stream);
        clip.start();
    }


}
