package edu.jhuapl.trinity.javafx.events;

/*-
 * #%L
 * trinity-1.0.0-SNAPSHOT
 * %%
 * Copyright (C) 2021 - 2023 The Johns Hopkins University Applied Physics Laboratory LLC
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/**
 * @author Sean Phillips
 */
public class ManifoldEvent extends Event {

    public Object object1 = null;
    public Object object2 = null;

    public static enum POINT_SOURCE {HYPERSPACE, HYPERSURFACE}

    public static final EventType<ManifoldEvent> GENERATE_NEW_UMAP = new EventType(ANY, "GENERATE_NEW_UMAP");
    public static final EventType<ManifoldEvent> USE_AUTOMATIC_TOLERANCE = new EventType(ANY, "USE_AUTOMATIC_TOLERANCE");
    public static final EventType<ManifoldEvent> SET_DISTANCE_TOLERANCE = new EventType(ANY, "SET_DISTANCE_TOLERANCE");
    public static final EventType<ManifoldEvent> GENERATE_HYPERSPACE_MANIFOLD = new EventType(ANY, "GENERATE_HYPERSPACE_MANIFOLD");
    public static final EventType<ManifoldEvent> GENERATE_PROJECTION_MANIFOLD = new EventType(ANY, "GENERATE_PROJECTION_MANIFOLD");
    public static final EventType<ManifoldEvent> CLEAR_ALL_MANIFOLDS = new EventType(ANY, "CLEAR_ALL_MANIFOLDS");
    public static final EventType<ManifoldEvent> USE_VISIBLE_POINTS = new EventType(ANY, "USE_VISIBLE_POINTS");
    public static final EventType<ManifoldEvent> USE_ALL_POINTS = new EventType(ANY, "USE_ALL_POINTS");
    public static final EventType<ManifoldEvent> MANIFOLD_SET_SCALE = new EventType(ANY, "MANIFOLD_SET_SCALE");
    public static final EventType<ManifoldEvent> MANIFOLD_SET_YAWPITCHROLL = new EventType(ANY, "MANIFOLD_SET_YAWPITCHROLL");
    public static final EventType<ManifoldEvent> MANIFOLD_ROTATE_X = new EventType(ANY, "MANIFOLD_ROTATE_X");
    public static final EventType<ManifoldEvent> MANIFOLD_ROTATE_Y = new EventType(ANY, "MANIFOLD_ROTATE_Y");
    public static final EventType<ManifoldEvent> MANIFOLD_ROTATE_Z = new EventType(ANY, "MANIFOLD_ROTATE_Z");
    public static final EventType<ManifoldEvent> MANIFOLD_DIFFUSE_COLOR = new EventType(ANY, "MANIFOLD_DIFFUSE_COLOR");
    public static final EventType<ManifoldEvent> MANIFOLD_SPECULAR_COLOR = new EventType(ANY, "MANIFOLD_SPECULAR_COLOR");
    public static final EventType<ManifoldEvent> MANIFOLD_WIREFRAME_COLOR = new EventType(ANY, "MANIFOLD_WIREFRAME_COLOR");
    public static final EventType<ManifoldEvent> MANIFOLD_FRONT_CULLFACE = new EventType(ANY, "MANIFOLD_FRONT_CULLFACE");
    public static final EventType<ManifoldEvent> MANIFOLD_BACK_CULLFACE = new EventType(ANY, "MANIFOLD_BACK_CULLFACE");
    public static final EventType<ManifoldEvent> MANIFOLD_NONE_CULLFACE = new EventType(ANY, "MANIFOLD_NONE_CULLFACE");
    public static final EventType<ManifoldEvent> MANIFOLD_FILL_DRAWMODE = new EventType(ANY, "MANIFOLD_FILL_DRAWMODE");
    public static final EventType<ManifoldEvent> MANIFOLD_LINE_DRAWMODE = new EventType(ANY, "MANIFOLD_LINE_DRAWMODE");
    public static final EventType<ManifoldEvent> MANIFOLD_SHOW_WIREFRAME = new EventType(ANY, "MANIFOLD_SHOW_WIREFRAME");
    public static final EventType<ManifoldEvent> MANIFOLD_SHOW_CONTROL = new EventType(ANY, "MANIFOLD_SHOW_CONTROL");

    public ManifoldEvent(EventType<? extends Event> arg0) {
        super(arg0);
    }

    public ManifoldEvent(EventType<? extends Event> arg0, Object arg1) {
        this(arg0);
        object1 = arg1;
    }

    public ManifoldEvent(EventType<? extends Event> arg0, Object arg1, Object arg2) {
        this(arg0);
        object1 = arg1;
        object2 = arg2;
    }

    public ManifoldEvent(Object arg0, EventTarget arg1, EventType<? extends Event> arg2) {
        super(arg0, arg1, arg2);
        object1 = arg0;
    }
}
