//
// $Id$
//
// Nenya library - tools for developing networked games
// Copyright (C) 2002-2010 Three Rings Design, Inc., All Rights Reserved
// http://code.google.com/p/nenya/
//
// This library is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published
// by the Free Software Foundation; either version 2.1 of the License, or
// (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package com.threerings.jme.tools.xml;

import java.io.FileInputStream;
import java.io.IOException;

import org.xml.sax.SAXException;
import org.apache.commons.digester.Digester;

import com.samskivert.xml.SetPropertyFieldsRule;

import com.threerings.jme.tools.AnimationDef;

/**
 * Parses XML files containing animations.
 */
public class AnimationParser
{
    public AnimationParser ()
    {
        // create and configure our digester
        _digester = new Digester();
        
        // add the rules
        String anim = "animation";
        _digester.addObjectCreate(anim, AnimationDef.class.getName());
        _digester.addRule(anim, new SetPropertyFieldsRule());
        _digester.addSetNext(anim, "setAnimation",
            AnimationDef.class.getName());
        
        String frame = anim + "/frame";
        _digester.addObjectCreate(frame,
            AnimationDef.FrameDef.class.getName());
        _digester.addSetNext(frame, "addFrame",
            AnimationDef.FrameDef.class.getName());
        
        String xform = frame + "/transform";
        _digester.addObjectCreate(xform,
            AnimationDef.TransformDef.class.getName());
        _digester.addRule(xform, new SetPropertyFieldsRule());
        _digester.addSetNext(xform, "addTransform",
            AnimationDef.TransformDef.class.getName());
    }
    
    /**
     * Parses the XML file at the specified path into an animation
     * definition.
     */
    public AnimationDef parseAnimation (String path)
        throws IOException, SAXException
    {
        _animation = null;
        _digester.push(this);
        _digester.parse(new FileInputStream(path));
        return _animation;
    }
    
    /**
     * Called by the parser once the animation is parsed.
     */
    public void setAnimation (AnimationDef animation)
    {
        _animation = animation;
    }
    
    protected Digester _digester;
    protected AnimationDef _animation;
}
