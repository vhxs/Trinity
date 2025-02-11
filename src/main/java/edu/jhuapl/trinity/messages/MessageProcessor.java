package edu.jhuapl.trinity.messages;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.jhuapl.trinity.data.FactorAnalysisState;
import edu.jhuapl.trinity.data.messages.ChannelFrame;
import edu.jhuapl.trinity.data.messages.FeatureVector;
import edu.jhuapl.trinity.data.messages.GaussianMixture;
import edu.jhuapl.trinity.data.messages.LabelConfig;
import edu.jhuapl.trinity.javafx.events.ChannelFrameDataEvent;
import edu.jhuapl.trinity.javafx.events.FactorAnalysisDataEvent;
import edu.jhuapl.trinity.javafx.events.FeatureVectorEvent;
import edu.jhuapl.trinity.javafx.events.GaussianMixtureEvent;
import javafx.application.Platform;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * @author Sean Phillips
 */
public class MessageProcessor {
    /**
     * Provides deserializaton support for JSON messages
     */
    private ObjectMapper mapper;
    /**
     * Scene reference that enables the processor to route data to events
     */
    private Scene scene;

    public MessageProcessor() {
        this(null);
    }

    public MessageProcessor(Scene scene) {
        this.scene = scene;
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // force serialization of timestamps as ISO-8601 standard. Assumption is most formats are ISO-8601 standard.
        // Also used later during conversion from objects by jackson.
//        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        mapper.registerModule(new JavaTimeModule());
    }

    public void process(String message) throws JsonProcessingException, IOException {
        if (ChannelFrame.isChannelFrame(message)) {
            ChannelFrame frame = getMapper().readValue(message, ChannelFrame.class);
            System.out.println("Frame: " + frame.getFrameId());
            System.out.println("Channel Values: " + frame.getChannelData());
            //@fire event to load data in JavaFX Scene
            Platform.runLater(() -> {
                scene.getRoot().fireEvent(new ChannelFrameDataEvent(frame));
            });
        } else if (FactorAnalysisState.isFactorAnalysisState(message)) {
            FactorAnalysisState fas = getMapper().readValue(message, FactorAnalysisState.class);
            System.out.println("Frame ID: " + fas.getFrameId());
            System.out.println("Factor Values: " + fas.getFactors());
            //@fire event to load data in JavaFX Scene
            Platform.runLater(() -> {
                scene.getRoot().fireEvent(new FactorAnalysisDataEvent(fas));
            });
        } else if (FeatureVector.isFeatureVector(message)) {
            FeatureVector featureVector = getMapper().readValue(message, FeatureVector.class);
            //@DEBUG SMP helpful debug prints
            //System.out.println("Image URL: " + object.getImageURL());
            //System.out.println("Feature Vector: " + object.getData());
            //fire event to load data in JavaFX Scene
            //Platform.runLater(() -> {
            scene.getRoot().fireEvent(new FeatureVectorEvent(
                FeatureVectorEvent.NEW_FEATURE_VECTOR, featureVector));
            //});
        } else if (GaussianMixture.isGaussianMixture(message)) {
            GaussianMixture gaussianMixture = getMapper().readValue(message, GaussianMixture.class);
            //@DEBUG SMP helpful debug prints
            //System.out.println("GaussianMixture Msg Received: " + gaussianMixture.getCovarianceMode());
            //fire event to load data in JavaFX Scene
            Platform.runLater(() -> {
                scene.getRoot().fireEvent(new GaussianMixtureEvent(
                    GaussianMixtureEvent.NEW_GAUSSIAN_MIXTURE, gaussianMixture));
            });
        } else if (LabelConfig.isLabelConfig(message)) {
            LabelConfig labelConfig = getMapper().readValue(message, LabelConfig.class);
            //@DEBUG SMP helpful debug prints
            //System.out.println("GaussianMixture Msg Received: " + gaussianMixture.getCovarianceMode());
            //fire event to load data in JavaFX Scene
            Platform.runLater(() -> {
                scene.getRoot().fireEvent(new FeatureVectorEvent(
                    FeatureVectorEvent.NEW_LABEL_CONFIG, labelConfig));
            });
        }
    }

    /**
     * @return the mapper
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

    /**
     * @param mapper the mapper to set
     */
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * @return the scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * @param scene the scene to set
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
