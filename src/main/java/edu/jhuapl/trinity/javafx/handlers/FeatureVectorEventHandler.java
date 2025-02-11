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
package edu.jhuapl.trinity.javafx.handlers;

import edu.jhuapl.trinity.App;
import edu.jhuapl.trinity.data.FactorLabel;
import edu.jhuapl.trinity.data.FeatureLayer;
import edu.jhuapl.trinity.data.messages.FeatureCollection;
import edu.jhuapl.trinity.data.messages.FeatureVector;
import edu.jhuapl.trinity.data.messages.LabelConfig;
import edu.jhuapl.trinity.javafx.components.ColorMap;
import edu.jhuapl.trinity.javafx.events.CommandTerminalEvent;
import edu.jhuapl.trinity.javafx.events.FeatureVectorEvent;
import edu.jhuapl.trinity.javafx.events.HyperspaceEvent;
import edu.jhuapl.trinity.javafx.renderers.FeatureVectorRenderer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sean Phillips
 */
public class FeatureVectorEventHandler implements EventHandler<FeatureVectorEvent> {

    public boolean pcaEnabled;
    List<FeatureVectorRenderer> renderers;
    public double[][] weights = null;
    ColorMap labelColorMap;
    ColorMap layerColorMap;
    int labelColorIndex = 0;
    int labelColorCount = 0;
    int layerColorIndex = 0;
    int layerColorCount = 0;

    public FeatureVectorEventHandler(boolean pcaEnabled) {
        this.pcaEnabled = pcaEnabled;
        renderers = new ArrayList<>();
        labelColorMap = ColorMap.tableau();
        labelColorCount = labelColorMap.getFixedColorCount();
        layerColorMap = ColorMap.jet();
        layerColorCount = labelColorMap.getFixedColorCount();
    }

    public void addFeatureVectorRenderer(FeatureVectorRenderer renderer) {
        renderers.add(renderer);
    }

    public void handleFeatureVectorEvent(FeatureVectorEvent event) {
        FeatureVector featureVector = (FeatureVector) event.object;
        if (event.getEventType().equals(FeatureVectorEvent.LOCATE_FEATURE_VECTOR)) {
            for (FeatureVectorRenderer renderer : renderers) {
                renderer.locateFeatureVector(featureVector);
            }
        }
        if (event.getEventType().equals(FeatureVectorEvent.NEW_FEATURE_VECTOR)) {
            //Have we seen this label before?
            FactorLabel matchingLabel = FactorLabel.getFactorLabel(featureVector.getLabel());
            //The label is new... add a new FactorLabel row
            if (null == matchingLabel) {
                if (labelColorIndex > labelColorCount) {
                    labelColorIndex = 0;
                }
                FactorLabel fl = new FactorLabel(featureVector.getLabel(),
                    labelColorMap.getColorByIndex(labelColorIndex));
                FactorLabel.addFactorLabel(fl);
                labelColorIndex++;
            }
            //Have we seen this layer before?
            int index = featureVector.getLayer();
            FeatureLayer matchingLayer = FeatureLayer.getFeatureLayer(index);
            //The layer is new... add a new FeatureLayer  row
            if (null == matchingLayer) {
                if (layerColorIndex > layerColorCount) {
                    layerColorIndex = 0;
                }
                FeatureLayer fl = new FeatureLayer(index,
                    layerColorMap.getColorByIndex(layerColorIndex));
                FeatureLayer.addFeatureLayer(fl);
                layerColorIndex++;
            }

//            //System.out.println("Image URL: " + object.getImageURL());
//            //System.out.println("Feature Vector: " + object.getData());
//            double [] singularValues = null;
//            if(pcaEnabled) {
//                if(null != weights) {
//                    double [][] multMatrix = AnalysisUtils.featuresMultWeights(
//                        FeatureVector.mapToStateArray.apply(featureVector), weights);
//                    singularValues = AnalysisUtils.doCommonsSVD(multMatrix);
//
//                } else {
//                    Logger.getLogger(FeatureVectorEventHandler.class.getName())
//                        .log(Level.WARNING, "PCA Enabled but weights matrix not set.");
//                }
//            }
            for (FeatureVectorRenderer renderer : renderers) {
//                if(null != singularValues) {
//                    renderer.addSingularValues(singularValues);
//                }
                renderer.addFeatureVector(featureVector);
            }
        }
    }

    public void scanLabelsAndLayers(List<FeatureVector> featureVectors) {
        featureVectors.forEach(featureVector -> {
            //Have we seen this label before?
            FactorLabel matchingLabel = FactorLabel.getFactorLabel(featureVector.getLabel());
            //The label is new... add a new FactorLabel row
            if (null == matchingLabel) {
                if (labelColorIndex > labelColorCount) {
                    labelColorIndex = 0;
                }
                FactorLabel fl = new FactorLabel(featureVector.getLabel(),
                    labelColorMap.getColorByIndex(labelColorIndex));
                FactorLabel.addFactorLabel(fl);
                labelColorIndex++;
            }
            //Have we seen this layer before?
            int index = featureVector.getLayer();
            FeatureLayer matchingLayer = FeatureLayer.getFeatureLayer(index);
            //The layer is new... add a new FeatureLayer row
            if (null == matchingLayer) {
                if (layerColorIndex > layerColorCount) {
                    layerColorIndex = 0;
                }
                FeatureLayer fl = new FeatureLayer(index,
                    layerColorMap.getColorByIndex(layerColorIndex));
                FeatureLayer.addFeatureLayer(fl);
                layerColorIndex++;
            }
        });
    }

    public void handleFeatureCollectionEvent(FeatureVectorEvent event) {
        FeatureCollection featureCollection = (FeatureCollection) event.object;
        if (null == featureCollection || featureCollection.getFeatures().isEmpty())
            return;
        Platform.runLater(() -> {
            App.getAppScene().getRoot().fireEvent(
                new CommandTerminalEvent("Scanning Feature Collection for labels...",
                    new Font("Consolas", 20), Color.GREEN));
        });

        //update labels
        scanLabelsAndLayers(featureCollection.getFeatures());

        for (FeatureVectorRenderer renderer : renderers) {
            renderer.addFeatureCollection(featureCollection);
        }
    }

    public void handleLabelConfigEvent(FeatureVectorEvent event) {
        LabelConfig labelConfig = (LabelConfig) event.object;
        if (null == labelConfig)
            return;
        if (null != labelConfig.isClearAll() && labelConfig.isClearAll()) {
            Platform.runLater(() -> {
                App.getAppScene().getRoot().fireEvent(
                    new CommandTerminalEvent("All Current Labels Cleared.",
                        new Font("Consolas", 20), Color.GREEN));
            });
            FactorLabel.removeAllFactorLabels();
        }
        if (null != labelConfig.getWildcards() && !labelConfig.getWildcards().isEmpty()) {
            Platform.runLater(() -> {
                App.getAppScene().getRoot().fireEvent(
                    new CommandTerminalEvent("Applying Wild Card Pattern Match Color Map...",
                        new Font("Consolas", 20), Color.GREEN));
            });
            //Going through each label in the system, try to apply each wildcard pattern
            //Yes if subsequent patterns match they will overwrite previous matches
            List<FactorLabel> updatedFactorLabels = new ArrayList<>();
            FactorLabel.getFactorLabels().stream().forEach(factorLabel -> {
                labelConfig.getWildcards().forEach((p, c) -> {
                    try {
                        Color parsedColor = Color.valueOf(c);
                        if (LabelConfig.isMatch(factorLabel.getLabel(), p)) {
                            updatedFactorLabels.add(factorLabel);
                            factorLabel.setColor(parsedColor);
                        }
                    } catch (Exception ex) {
                        //Matches....Matches?? We don't need no stinkin Matches!!
                        System.out.println("Could not convert " + p + " : " + c + " to valid pattern map");
                    }
                });
            });
            if (!updatedFactorLabels.isEmpty())
                Platform.runLater(() -> {
                    App.getAppScene().getRoot().fireEvent(new HyperspaceEvent(
                        HyperspaceEvent.UPDATEDALL_FACTOR_LABELS, updatedFactorLabels));
                });
        }
        //These are explicit label to color maps. This will overwrite any pattern matches
        if (null != labelConfig.getLabels() && !labelConfig.getLabels().isEmpty()) {
            Platform.runLater(() -> {
                App.getAppScene().getRoot().fireEvent(
                    new CommandTerminalEvent("Applying Explicit Label Color Map...",
                        new Font("Consolas", 20), Color.GREEN));
            });
            List<FactorLabel> newFactorLabels = new ArrayList<>();
            labelConfig.getLabels().forEach((l, c) -> {
                try {
                    Color parsedColor = Color.valueOf(c);
                    FactorLabel fl = new FactorLabel(l, parsedColor);
                    newFactorLabels.add(fl);
                } catch (Exception ex) {
                    System.out.println("Could not convert " + l + " : " + c + " to a Factor Label");
                }
            });
            if (!newFactorLabels.isEmpty())
                FactorLabel.addAllFactorLabels(newFactorLabels);
        }
    }

    @Override
    public void handle(FeatureVectorEvent event) {
        if (event.getEventType().equals(FeatureVectorEvent.NEW_FEATURE_VECTOR)
            || event.getEventType().equals(FeatureVectorEvent.LOCATE_FEATURE_VECTOR))
            handleFeatureVectorEvent(event);
        else if (event.getEventType().equals(FeatureVectorEvent.NEW_FEATURE_COLLECTION))
            handleFeatureCollectionEvent(event);
        else if (event.getEventType().equals(FeatureVectorEvent.NEW_LABEL_CONFIG))
            handleLabelConfigEvent(event);
        else if (event.getEventType().equals(FeatureVectorEvent.RESCAN_FACTOR_LABELS)
            || event.getEventType().equals(FeatureVectorEvent.RESCAN_FEATURE_LAYERS)) {
            labelColorIndex = 0;
            for (FeatureVectorRenderer renderer : renderers) {
                //FeatureCollection fc = (FeatureCollection) event.object;
                scanLabelsAndLayers(renderer.getAllFeatureVectors());
                renderer.refresh();
            }
        }
    }
}
