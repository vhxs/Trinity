package edu.jhuapl.trinity.javafx.renderers;

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

import edu.jhuapl.trinity.javafx.javafx3d.Trajectory3D;
import javafx.scene.Group;

/**
 * @author Sean Phillips
 */
public interface TrajectoryRenderer {
    public void addTrajectory(Group trajGroup, Group pointGroup, Trajectory3D trajectory3D);

    public void addTrajectoryGroup(Group newTrajGroup, Group newTrajPointGroup);

    //    public Trajectory
    public void clearTrajectories();
}
