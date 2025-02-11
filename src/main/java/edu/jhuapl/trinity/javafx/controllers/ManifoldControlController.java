package edu.jhuapl.trinity.javafx.controllers;

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

import edu.jhuapl.trinity.App;
import edu.jhuapl.trinity.data.FactorLabel;
import edu.jhuapl.trinity.javafx.events.ManifoldEvent;
import edu.jhuapl.trinity.utils.umap.Umap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Sean Phillips
 */
public class ManifoldControlController implements Initializable {
    //Geometry Tab
    @FXML
    private RadioButton useVisibleRadioButton;
    @FXML
    private RadioButton useAllRadioButton;
    ToggleGroup pointsToggleGroup;
    @FXML
    private ChoiceBox labelChoiceBox;

    @FXML
    private CheckBox automaticCheckBox;
    @FXML
    private Spinner manualSpinner;
    @FXML
    private Slider scaleSlider;
    @FXML
    private Slider rotateXSlider;
    @FXML
    private Slider rotateYSlider;
    @FXML
    private Slider rotateZSlider;
    @FXML
    private Label scaleLabel;
    @FXML
    private Label rotateXLabel;
    @FXML
    private Label rotateYLabel;
    @FXML
    private Label rotateZLabel;
    @FXML
    private ColorPicker manifoldDiffuseColorPicker;
    @FXML
    private ColorPicker manifoldSpecularColorPicker;
    @FXML
    private ColorPicker manifoldWireMeshColorPicker;
    @FXML
    private RadioButton frontCullFaceRadioButton;
    @FXML
    private RadioButton backCullFaceRadioButton;
    @FXML
    private RadioButton noneCullFaceRadioButton;
    ToggleGroup cullfaceToggleGroup;
    @FXML
    private RadioButton fillDrawModeRadioButton;
    @FXML
    private RadioButton linesDrawModeRadioButton;
    ToggleGroup drawModeToggleGroup;
    @FXML
    private CheckBox showWireframeCheckBox;
    @FXML
    private CheckBox showControlPointsCheckBox;

    //UMAP tab
    @FXML
    private Slider repulsionSlider;
    @FXML
    private Slider minDistanceSlider;
    @FXML
    private Slider spreadSlider;
    @FXML
    private Slider opMixSlider;
    @FXML
    private Spinner numComponentsSpinner;
    @FXML
    private Spinner numEpochsSpinner;
    @FXML
    private Spinner nearestNeighborsSpinner;
    @FXML
    private Spinner negativeSampleRateSpinner;
    @FXML
    private Spinner localConnectivitySpinner;
    @FXML
    private ChoiceBox metricChoiceBox;
    @FXML
    private CheckBox verboseCheckBox;
    //Geometry Tab
    @FXML
    private RadioButton useHyperspaceButton;
    @FXML
    private RadioButton useHypersurfaceButton;
    ToggleGroup hyperSourceGroup;

    Scene scene;
    private final String ALL = "ALL";
    /**
     * Format for floating coordinate label
     */
    private NumberFormat format = new DecimalFormat("0.00");

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scene = App.getAppScene();
        setupHullControls();
        setupUmapControls();
    }

    private void getCurrentLabels() {
        labelChoiceBox.getItems().clear();
        labelChoiceBox.getItems().add(ALL);
        labelChoiceBox.getItems().addAll(
            FactorLabel.getFactorLabels().stream().map(f -> f.getLabel()).toList());
    }

    private void setupUmapControls() {
        metricChoiceBox.getItems().addAll("euclidean", "manhattan", "chebyshev",
            "minkowski", "canberra", "braycurtis", "cosine", "correlation",
            "haversine", "hamming", "jaccard", "dice", "russellrao",
            "kulsinski", "rogerstanimoto", "sokalmichener", "sokalsneath", "yule");
        metricChoiceBox.getSelectionModel().selectFirst();

        numComponentsSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 100, 3, 1));
        numEpochsSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(25, 500, 200, 25));
        nearestNeighborsSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 100, 15, 5));
        negativeSampleRateSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 5, 1));
        localConnectivitySpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 1, 1));

        hyperSourceGroup = new ToggleGroup();
        useHyperspaceButton.setToggleGroup(hyperSourceGroup);
        useHypersurfaceButton.setToggleGroup(hyperSourceGroup);
    }

    private void setupHullControls() {
        getCurrentLabels();
        labelChoiceBox.getSelectionModel().selectFirst();
        labelChoiceBox.setOnShown(e -> getCurrentLabels());
        manualSpinner.setValueFactory(
            new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 1, 0.5, 0.1));
        manualSpinner.setEditable(true);
        //whenever the spinner value is changed...
        manualSpinner.valueProperty().addListener(e -> {
            scene.getRoot().fireEvent(
                new ManifoldEvent(ManifoldEvent.SET_DISTANCE_TOLERANCE,
                    (Double) manualSpinner.getValue()));
        });
        manualSpinner.disableProperty().bind(automaticCheckBox.selectedProperty());

        scaleLabel.setText(format.format(scaleSlider.getValue()));
        scaleSlider.valueProperty().addListener((ov, t, t1) -> {
            scaleLabel.setText(format.format(scaleSlider.getValue()));
            scene.getRoot().fireEvent(new ManifoldEvent(
                ManifoldEvent.MANIFOLD_SET_SCALE, t.doubleValue()));
        });

        rotateXLabel.setText(format.format(rotateXSlider.getValue()));
        rotateXSlider.valueProperty().addListener((ov, t, t1) -> {
            rotateXLabel.setText(format.format(rotateXSlider.getValue()));
            double[] ypr = new double[]{
                rotateYSlider.getValue(), rotateXSlider.getValue(), rotateZSlider.getValue()
            };
            scene.getRoot().fireEvent(new ManifoldEvent(
                ManifoldEvent.MANIFOLD_SET_YAWPITCHROLL, ypr));
        });

        rotateYLabel.setText(format.format(rotateYSlider.getValue()));
        rotateYSlider.valueProperty().addListener((ov, t, t1) -> {
            rotateYLabel.setText(format.format(rotateYSlider.getValue()));
            double[] ypr = new double[]{
                rotateYSlider.getValue(), rotateXSlider.getValue(), rotateZSlider.getValue()
            };
            scene.getRoot().fireEvent(new ManifoldEvent(
                ManifoldEvent.MANIFOLD_SET_YAWPITCHROLL, ypr));
        });

        rotateZLabel.setText(format.format(rotateZSlider.getValue()));
        rotateZSlider.valueProperty().addListener((ov, t, t1) -> {
            rotateZLabel.setText(format.format(rotateZSlider.getValue()));
            double[] ypr = new double[]{
                rotateYSlider.getValue(), rotateXSlider.getValue(), rotateZSlider.getValue()
            };
            scene.getRoot().fireEvent(new ManifoldEvent(
                ManifoldEvent.MANIFOLD_SET_YAWPITCHROLL, ypr));
        });

        manifoldDiffuseColorPicker.setValue(Color.CYAN);
        manifoldDiffuseColorPicker.valueProperty().addListener(cl -> {
            scene.getRoot().fireEvent(new ManifoldEvent(
                ManifoldEvent.MANIFOLD_DIFFUSE_COLOR,
                manifoldDiffuseColorPicker.getValue()));
        });
        manifoldSpecularColorPicker.setValue(Color.RED);
        manifoldSpecularColorPicker.valueProperty().addListener(cl -> {
            scene.getRoot().fireEvent(new ManifoldEvent(
                ManifoldEvent.MANIFOLD_SPECULAR_COLOR,
                manifoldSpecularColorPicker.getValue()));
        });
        manifoldWireMeshColorPicker.setValue(Color.BLUE);
        manifoldWireMeshColorPicker.valueProperty().addListener(cl -> {
            scene.getRoot().fireEvent(new ManifoldEvent(
                ManifoldEvent.MANIFOLD_WIREFRAME_COLOR,
                manifoldWireMeshColorPicker.getValue()));
        });

        pointsToggleGroup = new ToggleGroup();
        useVisibleRadioButton.setToggleGroup(pointsToggleGroup);
        useAllRadioButton.setToggleGroup(pointsToggleGroup);
        pointsToggleGroup.selectedToggleProperty().addListener(cl -> {
            if (useVisibleRadioButton.isSelected())
                scene.getRoot().fireEvent(new ManifoldEvent(
                    ManifoldEvent.USE_VISIBLE_POINTS, true));
            else
                scene.getRoot().fireEvent(new ManifoldEvent(
                    ManifoldEvent.USE_ALL_POINTS, true));
        });

        cullfaceToggleGroup = new ToggleGroup();
        frontCullFaceRadioButton.setToggleGroup(cullfaceToggleGroup);
        backCullFaceRadioButton.setToggleGroup(cullfaceToggleGroup);
        noneCullFaceRadioButton.setToggleGroup(cullfaceToggleGroup);
        cullfaceToggleGroup.selectedToggleProperty().addListener(cl -> {
            if (frontCullFaceRadioButton.isSelected())
                scene.getRoot().fireEvent(new ManifoldEvent(
                    ManifoldEvent.MANIFOLD_FRONT_CULLFACE, true));
            else if (backCullFaceRadioButton.isSelected())
                scene.getRoot().fireEvent(new ManifoldEvent(
                    ManifoldEvent.MANIFOLD_BACK_CULLFACE, true));
            else
                scene.getRoot().fireEvent(new ManifoldEvent(
                    ManifoldEvent.MANIFOLD_NONE_CULLFACE, true));
        });
        drawModeToggleGroup = new ToggleGroup();
        fillDrawModeRadioButton.setToggleGroup(drawModeToggleGroup);
        linesDrawModeRadioButton.setToggleGroup(drawModeToggleGroup);
        drawModeToggleGroup.selectedToggleProperty().addListener(cl -> {
            if (fillDrawModeRadioButton.isSelected())
                scene.getRoot().fireEvent(new ManifoldEvent(
                    ManifoldEvent.MANIFOLD_FILL_DRAWMODE, true));
            else
                scene.getRoot().fireEvent(new ManifoldEvent(
                    ManifoldEvent.MANIFOLD_LINE_DRAWMODE, true));
        });

        showWireframeCheckBox.selectedProperty().addListener(cl -> {
            scene.getRoot().fireEvent(new ManifoldEvent(
                ManifoldEvent.MANIFOLD_SHOW_WIREFRAME, showWireframeCheckBox.isSelected()));
        });
        showControlPointsCheckBox.selectedProperty().addListener(cl -> {
            scene.getRoot().fireEvent(new ManifoldEvent(
                ManifoldEvent.MANIFOLD_SHOW_CONTROL, showControlPointsCheckBox.isSelected()));
        });
    }

    @FXML
    public void project() {
        Umap umap = new Umap();
        umap.setRepulsionStrength((float) repulsionSlider.getValue());
        umap.setMinDist((float) minDistanceSlider.getValue());
        umap.setSpread((float) spreadSlider.getValue());
        umap.setSetOpMixRatio((float) opMixSlider.getValue());
        umap.setNumberComponents((int) numComponentsSpinner.getValue());
        umap.setNumberEpochs((int) numEpochsSpinner.getValue());
        umap.setNumberNearestNeighbours((int) nearestNeighborsSpinner.getValue());
        umap.setNegativeSampleRate((int) negativeSampleRateSpinner.getValue());
        umap.setLocalConnectivity((int) localConnectivitySpinner.getValue());
        umap.setMetric((String) metricChoiceBox.getValue());
        umap.setVerbose(verboseCheckBox.isSelected());
        ManifoldEvent.POINT_SOURCE pointSource = useHypersurfaceButton.isSelected() ?
            ManifoldEvent.POINT_SOURCE.HYPERSURFACE : ManifoldEvent.POINT_SOURCE.HYPERSPACE;
        scene.getRoot().fireEvent(new ManifoldEvent(
            ManifoldEvent.GENERATE_NEW_UMAP, umap, pointSource));
    }

    @FXML
    public void generate() {
        scene.getRoot().fireEvent(new ManifoldEvent(
            ManifoldEvent.GENERATE_HYPERSPACE_MANIFOLD, useVisibleRadioButton.isSelected(), (String) labelChoiceBox.getValue()));
    }

    @FXML
    public void clearAll() {
        scene.getRoot().fireEvent(new ManifoldEvent(
            ManifoldEvent.CLEAR_ALL_MANIFOLDS));
    }
}
