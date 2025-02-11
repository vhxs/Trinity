package edu.jhuapl.trinity.data;

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

//import com.aparapi.Kernel;

import edu.jhuapl.trinity.data.messages.FeatureVector;

import java.util.Arrays;
import java.util.List;

/**
 * Experimental GPGPU kernel
 *
 * @author Sean Phillips
 */
public class FeatureKernel { // extends Kernel {

    public double[] xCoords;
    public double[] yCoords;
    public double[] zCoords;
    public double[] zMinusCoords;

    public double[] xFactors;
    public double[] yFactors;
    public double[] zFactors;

    private double pointScale, domainMin, domainWidth, rangeWidth;

    public FeatureKernel() {

    }

    public FeatureKernel(double[] xCoords, double[] yCoords, double[] zCoords, double[] zMinusCoords,
                         double[] xFactors, double[] yFactors, double[] zFactors) {
        setBuffers(xCoords, yCoords, zCoords, zMinusCoords, xFactors, yFactors, zFactors);
//        // set explicit video ram management and send the data to GPU once
//        setExplicit(true);
//        put(xCoords);
//        put(yCoords);
//        put(zCoords);
//        put(xFactors);
//        put(yFactors);
//        put(zFactors);
    }

    //    @Override
    public void run() {
//        int gid = this.getGlobalId();
        int gid = 0;
        xCoords[gid] = (((pointScale * xFactors[gid] - domainMin) * (rangeWidth)) / domainWidth);
        yCoords[gid] = (((pointScale * yFactors[gid] - domainMin) * (rangeWidth)) / domainWidth);
        zMinusCoords[gid] = (((pointScale * -zFactors[gid] - domainMin) * (rangeWidth)) / domainWidth);
        zCoords[gid] = (((pointScale * zFactors[gid] - domainMin) * (rangeWidth)) / domainWidth);
    }

    public void setFeatureVectors(List<FeatureVector> featureVectors, int x, int y, int z) {
        xFactors = new double[featureVectors.size()];
        yFactors = new double[featureVectors.size()];
        zFactors = new double[featureVectors.size()];
        Arrays.parallelSetAll(xFactors, i -> featureVectors.get(i).getData().get(x));
        Arrays.parallelSetAll(yFactors, i -> featureVectors.get(i).getData().get(y));
        Arrays.parallelSetAll(zFactors, i -> featureVectors.get(i).getData().get(z));

        xCoords = new double[featureVectors.size()];
        yCoords = new double[featureVectors.size()];
        zCoords = new double[featureVectors.size()];
        zMinusCoords = new double[featureVectors.size()];
    }

    public void setBuffers(double[] xCoords, double[] yCoords, double[] zCoords, double[] zMinusCoords,
                           double[] xFactors, double[] yFactors, double[] zFactors) {
        this.xCoords = xCoords;
        this.yCoords = yCoords;
        this.zCoords = zCoords;
        this.zMinusCoords = zMinusCoords;

        this.xFactors = xFactors;
        this.yFactors = yFactors;
        this.zFactors = zFactors;
    }

    public void setParameters(double pointScale, double domainMin, double domainWidth, double rangeWidth) {
        this.pointScale = pointScale;
        this.domainMin = domainMin;
        this.domainWidth = domainWidth;
        this.rangeWidth = rangeWidth;
    }
}
