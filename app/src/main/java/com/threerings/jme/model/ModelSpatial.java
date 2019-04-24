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

package com.threerings.jme.model;

import com.jme.renderer.Renderer;
import com.jme.scene.Spatial;

import com.threerings.jme.util.ShaderCache;

/**
 * Contains method common to both {@link ModelNode}s and {@link ModelMesh}es.
 */
public interface ModelSpatial
{
    /**
     * Recursively expands the model bounds of any deformable meshes so that
     * they include the current vertex positions.
     */
    public void expandModelBounds ();

    /**
     * Recursively sets the reference transforms for any bones in the model.
     */
    public void setReferenceTransforms ();

    /**
     * Recursively compiles any static meshes to vertex buffer objects (VBOs)
     * or display lists.
     *
     * @param useVBOs if true, use VBOs if the graphics card supports them
     * @param useDisplayLists if true and not using VBOs, compile static
     * objects to display lists
     */
    public void lockStaticMeshes (
        Renderer renderer, boolean useVBOs, boolean useDisplayLists);

    /**
     * Recursively resolves texture references using the given provider.
     */
    public void resolveTextures (TextureProvider tprov);

    /**
     * Recursively creates or reconfigures any shaders used by the model using the supplied cache.
     */
    public void configureShaders (ShaderCache scache);

    /**
     * Recursively requests that the current state of all skinned meshes be
     * stored as an animation frame on the next update.
     *
     * @param frameId the frame id, which uniquely identifies one frame of
     * one animation
     * @param blend whether or not the stored frames will be retrieved by
     * calls to {@link #blendMeshFrames} as opposed to {@link #setMeshFrame}
     */
    public void storeMeshFrame (int frameId, boolean blend);

    /**
     * Recursively switches all skinned meshes to a stored animation frame for
     * the next update.
     */
    public void setMeshFrame (int frameId);

    /**
     * Recursively blends all skinned meshes between two stored animation
     * frames for the next update.
     */
    public void blendMeshFrames (int frameId1, int frameId2, float alpha);

    /**
     * Creates or populates and returns a clone of this object using the given
     * clone properties.
     *
     * @param store an instance of this class to populate, or <code>null</code>
     * to create a new instance
     */
    public Spatial putClone (Spatial store, Model.CloneCreator properties);
}
