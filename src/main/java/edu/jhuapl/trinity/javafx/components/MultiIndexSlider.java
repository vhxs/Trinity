/**
 * Copyright (c) 2013, 2018 ControlsFX
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of ControlsFX, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.jhuapl.trinity.javafx.components;

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

//import impl.org.controlsfx.skin.RangeSliderSkin;
//import org.controlsfx.tools.Utils;

import edu.jhuapl.trinity.utils.Utils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.EnumConverter;
import javafx.css.converter.SizeConverter;
import javafx.geometry.Orientation;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @adapted ControlsFX 11.0.1
 * https://github.com/controlsfx/controlsfx/blob/master/controlsfx/src/main/java/org/controlsfx/control/RangeSlider.java
 * <p>
 * WIP smp
 */
public class MultiIndexSlider extends Control {

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new RangeSlider instance using default values of 0.0, 0.25, 0.75
     * and 1.0 for min/lowValue/highValue/max, respectively.
     */
    public MultiIndexSlider() {
        this(0, 1.0, 0.25, 0.75);
    }

    /**
     * Instantiates a default, horizontal RangeSlider with the specified
     * min/max/low/high values.
     *
     * @param min       The minimum allowable value that the RangeSlider will allow.
     * @param max       The maximum allowable value that the RangeSlider will allow.
     * @param lowValue  The initial value for the low value in the RangeSlider.
     * @param highValue The initial value for the high value in the RangeSlider.
     */
    public MultiIndexSlider(double min, double max, double lowValue, double highValue) {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);

        setMax(max);
        setMin(min);
        adjustValues();
        setLowValue(lowValue);
        setHighValue(highValue);
    }
//
//    /** {@inheritDoc} */
//    @Override public String getUserAgentStylesheet() {
//        return getUserAgentStylesheet(MultiIndexSlider.class, "rangeslider.css");
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override protected Skin<?> createDefaultSkin() {
//        return new RangeSliderSkin(this);
//    }


    /***************************************************************************
     *                                                                         *
     * New properties (over and above what is in Slider)                       *
     *                                                                         *
     **************************************************************************/

    // --- low value

    /**
     * The low value property represents the current position of the low value
     * thumb, and is within the allowable range as specified by the
     * {@link #minProperty() min} and {@link #maxProperty() max} properties. By
     * default this value is 0.
     */
    public final DoubleProperty lowValueProperty() {
        return lowValue;
    }

    private DoubleProperty lowValue = new SimpleDoubleProperty(this, "lowValue", 0.0D) { //$NON-NLS-1$
        @Override
        protected void invalidated() {
            adjustLowValues();
        }
    };

    /**
     * Sets the low value for the range slider, which may or may not be clamped
     * to be within the allowable range as specified by the
     * {@link #minProperty() min} and {@link #maxProperty() max} properties.
     */
    public final void setLowValue(double d) {
        lowValueProperty().set(d);
    }

    /**
     * Returns the current low value for the range slider.
     */
    public final double getLowValue() {
        return lowValue != null ? lowValue.get() : 0.0D;
    }


    // --- low value changing

    /**
     * When true, indicates the current low value of this RangeSlider is changing.
     * It provides notification that the low value is changing. Once the low
     * value is computed, it is set back to false.
     */
    public final BooleanProperty lowValueChangingProperty() {
        if (lowValueChanging == null) {
            lowValueChanging = new SimpleBooleanProperty(this, "lowValueChanging", false); //$NON-NLS-1$
        }
        return lowValueChanging;
    }

    private BooleanProperty lowValueChanging;

    /**
     * Call this when the low value is changing.
     *
     * @param value True if the low value is changing, false otherwise.
     */
    public final void setLowValueChanging(boolean value) {
        lowValueChangingProperty().set(value);
    }

    /**
     * Returns whether or not the low value of this MultiIndexSlider is currently
     * changing.
     */
    public final boolean isLowValueChanging() {
        return lowValueChanging == null ? false : lowValueChanging.get();
    }


    // --- high value

    /**
     * The high value property represents the current position of the high value
     * thumb, and is within the allowable range as specified by the
     * {@link #minProperty() min} and {@link #maxProperty() max} properties. By
     * default this value is 100.
     */
    public final DoubleProperty highValueProperty() {
        return highValue;
    }

    private DoubleProperty highValue = new SimpleDoubleProperty(this, "highValue", 100D) { //$NON-NLS-1$
        @Override
        protected void invalidated() {
            adjustHighValues();
        }

        @Override
        public Object getBean() {
            return MultiIndexSlider.this;
        }

        @Override
        public String getName() {
            return "highValue"; //$NON-NLS-1$
        }
    };

    /**
     * Sets the high value for the range slider, which may or may not be clamped
     * to be within the allowable range as specified by the
     * {@link #minProperty() min} and {@link #maxProperty() max} properties.
     */
    public final void setHighValue(double d) {
        if (!highValueProperty().isBound()) highValueProperty().set(d);
    }

    /**
     * Returns the current high value for the range slider.
     */
    public final double getHighValue() {
        return highValue != null ? highValue.get() : 100D;
    }


    // --- high value changing

    /**
     * When true, indicates the current high value of this RangeSlider is changing.
     * It provides notification that the high value is changing. Once the high
     * value is computed, it is set back to false.
     */
    public final BooleanProperty highValueChangingProperty() {
        if (highValueChanging == null) {
            highValueChanging = new SimpleBooleanProperty(this, "highValueChanging", false); //$NON-NLS-1$
        }
        return highValueChanging;
    }

    private BooleanProperty highValueChanging;

    /**
     * Call this when high low value is changing.
     *
     * @param value True if the high value is changing, false otherwise.
     */
    public final void setHighValueChanging(boolean value) {
        highValueChangingProperty().set(value);
    }

    /**
     * Returns whether or not the high value of this MultiIndexSlider is currently
     * changing.
     */
    public final boolean isHighValueChanging() {
        return highValueChanging == null ? false : highValueChanging.get();
    }

    private final ObjectProperty<StringConverter<Number>> tickLabelFormatter = new SimpleObjectProperty<>();

    /**
     * Gets the value of the property tickLabelFormatter.
     *
     * @return the value of the property tickLabelFormatter.
     */
    public final StringConverter<Number> getLabelFormatter() {
        return tickLabelFormatter.get();
    }

    /**
     * Sets the value of the property tickLabelFormatter.
     *
     * @param value
     */
    public final void setLabelFormatter(StringConverter<Number> value) {
        tickLabelFormatter.set(value);
    }

    /**
     * StringConverter used to format tick mark labels. If null a default will be used.
     *
     * @return a Property containing the StringConverter.
     */
    public final ObjectProperty<StringConverter<Number>> labelFormatterProperty() {
        return tickLabelFormatter;
    }

    /***************************************************************************
     *                                                                         *
     * New public API                                                          *
     *                                                                         *
     **************************************************************************/

    /**
     * Increments the {@link #lowValueProperty() low value} by the
     * {@link #blockIncrementProperty() block increment} amount.
     */
    public void incrementLowValue() {
        adjustLowValue(getLowValue() + getBlockIncrement());
    }

    /**
     * Decrements the {@link #lowValueProperty() low value} by the
     * {@link #blockIncrementProperty() block increment} amount.
     */
    public void decrementLowValue() {
        adjustLowValue(getLowValue() - getBlockIncrement());
    }

    /**
     * Increments the {@link #highValueProperty() high value} by the
     * {@link #blockIncrementProperty() block increment} amount.
     */
    public void incrementHighValue() {
        adjustHighValue(getHighValue() + getBlockIncrement());
    }

    /**
     * Decrements the {@link #highValueProperty() high value} by the
     * {@link #blockIncrementProperty() block increment} amount.
     */
    public void decrementHighValue() {
        adjustHighValue(getHighValue() - getBlockIncrement());
    }

    /**
     * Adjusts {@link #lowValueProperty() lowValue} to match <code>newValue</code>,
     * or as closely as possible within the constraints imposed by the
     * {@link #minProperty() min} and {@link #maxProperty() max} properties.
     * This function also takes into account
     * {@link #snapToTicksProperty() snapToTicks}, which is the main difference
     * between <code>adjustLowValue</code> and
     * {@link #setLowValue(double) setLowValue}.
     */
    public void adjustLowValue(double newValue) {
        double d1 = getMin();
        double d2 = getMax();
        if (d2 <= d1) {
            // no-op
        } else {
            newValue = newValue >= d1 ? newValue : d1;
            newValue = newValue <= d2 ? newValue : d2;
            setLowValue(snapValueToTicks(newValue));
        }
    }

    /**
     * Adjusts {@link #highValueProperty() highValue} to match <code>newValue</code>,
     * or as closely as possible within the constraints imposed by the
     * {@link #minProperty() min} and {@link #maxProperty() max} properties.
     * This function also takes into account
     * {@link #snapToTicksProperty() snapToTicks}, which is the main difference
     * between <code>adjustHighValue</code> and
     * {@link #setHighValue(double) setHighValue}.
     */
    public void adjustHighValue(double newValue) {
        double d1 = getMin();
        double d2 = getMax();
        if (d2 <= d1) {
            // no-op
        } else {
            newValue = newValue >= d1 ? newValue : d1;
            newValue = newValue <= d2 ? newValue : d2;
            setHighValue(snapValueToTicks(newValue));
        }
    }


    /***************************************************************************
     *                                                                         *
     * Properties copied from Slider (and slightly edited)                     *
     *                                                                         *
     **************************************************************************/

    private DoubleProperty max;

    /**
     * Sets the maximum value for this Slider.
     *
     * @param value
     */
    public final void setMax(double value) {
        maxProperty().set(value);
    }

    /**
     * @return The maximum value of this slider. 100 is returned if
     * the maximum value has never been set.
     */
    public final double getMax() {
        return max == null ? 100 : max.get();
    }

    /**
     * @return A DoubleProperty representing the maximum value of this Slider.
     * This must be a value greater than {@link #minProperty() min}.
     */
    public final DoubleProperty maxProperty() {
        if (max == null) {
            max = new DoublePropertyBase(100) {
                @Override
                protected void invalidated() {
                    if (get() < getMin()) {
                        setMin(get());
                    }
                    adjustValues();
                }

                @Override
                public Object getBean() {
                    return MultiIndexSlider.this;
                }

                @Override
                public String getName() {
                    return "max"; //$NON-NLS-1$
                }
            };
        }
        return max;
    }

    private DoubleProperty min;

    /**
     * Sets the minimum value for this Slider.
     *
     * @param value
     */
    public final void setMin(double value) {
        minProperty().set(value);
    }

    /**
     * @return the minimum value for this Slider. 0 is returned if the minimum
     * has never been set.
     */
    public final double getMin() {
        return min == null ? 0 : min.get();
    }

    /**
     * @return A DoubleProperty representing The minimum value of this Slider.
     * This must be a value less than {@link #maxProperty() max}.
     */
    public final DoubleProperty minProperty() {
        if (min == null) {
            min = new DoublePropertyBase(0) {
                @Override
                protected void invalidated() {
                    if (get() > getMax()) {
                        setMax(get());
                    }
                    adjustValues();
                }

                @Override
                public Object getBean() {
                    return MultiIndexSlider.this;
                }

                @Override
                public String getName() {
                    return "min"; //$NON-NLS-1$
                }
            };
        }
        return min;
    }

    /**
     *
     */
    private BooleanProperty snapToTicks;

    /**
     * Sets the value of SnapToTicks.
     *
     * @param value
     * @see #snapToTicksProperty()
     */
    public final void setSnapToTicks(boolean value) {
        snapToTicksProperty().set(value);
    }

    /**
     * @return the value of SnapToTicks.
     * @see #snapToTicksProperty()
     */
    public final boolean isSnapToTicks() {
        return snapToTicks == null ? false : snapToTicks.get();
    }

    /**
     * Indicates whether the {@link #lowValueProperty()} value} /
     * {@link #highValueProperty()} value} of the {@code Slider} should always
     * be aligned with the tick marks. This is honored even if the tick marks
     * are not shown.
     *
     * @return A BooleanProperty.
     */
    public final BooleanProperty snapToTicksProperty() {
        if (snapToTicks == null) {
            snapToTicks = new StyleableBooleanProperty(false) {
                @Override
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return MultiIndexSlider.StyleableProperties.SNAP_TO_TICKS;
                }

                @Override
                public Object getBean() {
                    return MultiIndexSlider.this;
                }

                @Override
                public String getName() {
                    return "snapToTicks"; //$NON-NLS-1$
                }
            };
        }
        return snapToTicks;
    }

    /**
     *
     */
    private DoubleProperty majorTickUnit;

    /**
     * Sets the unit distance between major tick marks.
     *
     * @param value
     * @see #majorTickUnitProperty()
     */
    public final void setMajorTickUnit(double value) {
        if (value <= 0) {
            throw new IllegalArgumentException("MajorTickUnit cannot be less than or equal to 0."); //$NON-NLS-1$
        }
        majorTickUnitProperty().set(value);
    }

    /**
     * @return The unit distance between major tick marks.
     * @see #majorTickUnitProperty()
     */
    public final double getMajorTickUnit() {
        return majorTickUnit == null ? 25 : majorTickUnit.get();
    }

    /**
     * The unit distance between major tick marks. For example, if
     * the {@link #minProperty() min} is 0 and the {@link #maxProperty() max} is 100 and the
     * {@link #majorTickUnitProperty() majorTickUnit} is 25, then there would be 5 tick marks: one at
     * position 0, one at position 25, one at position 50, one at position
     * 75, and a final one at position 100.
     * <p>
     * This value should be positive and should be a value less than the
     * span. Out of range values are essentially the same as disabling
     * tick marks.
     *
     * @return A DoubleProperty
     */
    public final DoubleProperty majorTickUnitProperty() {
        if (majorTickUnit == null) {
            majorTickUnit = new StyleableDoubleProperty(25) {
                @Override
                public void invalidated() {
                    if (get() <= 0) {
                        throw new IllegalArgumentException("MajorTickUnit cannot be less than or equal to 0."); //$NON-NLS-1$
                    }
                }

                @Override
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.MAJOR_TICK_UNIT;
                }

                @Override
                public Object getBean() {
                    return MultiIndexSlider.this;
                }

                @Override
                public String getName() {
                    return "majorTickUnit"; //$NON-NLS-1$
                }
            };
        }
        return majorTickUnit;
    }

    /**
     *
     */
    private IntegerProperty minorTickCount;

    /**
     * Sets the number of minor ticks to place between any two major ticks.
     *
     * @param value
     * @see #minorTickCountProperty()
     */
    public final void setMinorTickCount(int value) {
        minorTickCountProperty().set(value);
    }

    /**
     * @return The number of minor ticks to place between any two major ticks.
     * @see #minorTickCountProperty()
     */
    public final int getMinorTickCount() {
        return minorTickCount == null ? 3 : minorTickCount.get();
    }

    /**
     * The number of minor ticks to place between any two major ticks. This
     * number should be positive or zero. Out of range values will disable
     * disable minor ticks, as will a value of zero.
     *
     * @return An InterProperty
     */
    public final IntegerProperty minorTickCountProperty() {
        if (minorTickCount == null) {
            minorTickCount = new StyleableIntegerProperty(3) {
                @Override
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return MultiIndexSlider.StyleableProperties.MINOR_TICK_COUNT;
                }

                @Override
                public Object getBean() {
                    return MultiIndexSlider.this;
                }

                @Override
                public String getName() {
                    return "minorTickCount"; //$NON-NLS-1$
                }
            };
        }
        return minorTickCount;
    }

    /**
     *
     */
    private DoubleProperty blockIncrement;

    /**
     * Sets the amount by which to adjust the slider if the track of the slider is
     * clicked.
     *
     * @param value
     * @see #blockIncrementProperty()
     */
    public final void setBlockIncrement(double value) {
        blockIncrementProperty().set(value);
    }

    /**
     * @return The amount by which to adjust the slider if the track of the slider is
     * clicked.
     * @see #blockIncrementProperty()
     */
    public final double getBlockIncrement() {
        return blockIncrement == null ? 10 : blockIncrement.get();
    }

    /**
     * The amount by which to adjust the slider if the track of the slider is
     * clicked. This is used when manipulating the slider position using keys. If
     * {@link #snapToTicksProperty() snapToTicks} is true then the nearest tick mark to the adjusted
     * value will be used.
     *
     * @return A DoubleProperty
     */
    public final DoubleProperty blockIncrementProperty() {
        if (blockIncrement == null) {
            blockIncrement = new StyleableDoubleProperty(10) {
                @Override
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return MultiIndexSlider.StyleableProperties.BLOCK_INCREMENT;
                }

                @Override
                public Object getBean() {
                    return MultiIndexSlider.this;
                }

                @Override
                public String getName() {
                    return "blockIncrement"; //$NON-NLS-1$
                }
            };
        }
        return blockIncrement;
    }

    /**
     *
     */
    private ObjectProperty<Orientation> orientation;

    /**
     * Sets the orientation of the Slider.
     *
     * @param value
     */
    public final void setOrientation(Orientation value) {
        orientationProperty().set(value);
    }

    /**
     * @return The orientation of the Slider. {@link Orientation#HORIZONTAL} is
     * returned by default.
     */
    public final Orientation getOrientation() {
        return orientation == null ? Orientation.HORIZONTAL : orientation.get();
    }

    /**
     * The orientation of the {@code Slider} can either be horizontal
     * or vertical.
     *
     * @return An Objectproperty representing the orientation of the Slider.
     */
    public final ObjectProperty<Orientation> orientationProperty() {
        if (orientation == null) {
            orientation = new StyleableObjectProperty<Orientation>(Orientation.HORIZONTAL) {
                @Override
                protected void invalidated() {
                    final boolean vertical = (get() == Orientation.VERTICAL);
                    pseudoClassStateChanged(VERTICAL_PSEUDOCLASS_STATE, vertical);
                    pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, !vertical);
                }

                @Override
                public CssMetaData<? extends Styleable, Orientation> getCssMetaData() {
                    return MultiIndexSlider.StyleableProperties.ORIENTATION;
                }

                @Override
                public Object getBean() {
                    return MultiIndexSlider.this;
                }

                @Override
                public String getName() {
                    return "orientation"; //$NON-NLS-1$
                }
            };
        }
        return orientation;
    }

    private BooleanProperty showTickLabels;

    /**
     * Sets whether labels of tick marks should be shown or not.
     *
     * @param value
     */
    public final void setShowTickLabels(boolean value) {
        showTickLabelsProperty().set(value);
    }

    /**
     * @return whether labels of tick marks are being shown.
     */
    public final boolean isShowTickLabels() {
        return showTickLabels == null ? false : showTickLabels.get();
    }

    /**
     * Indicates that the labels for tick marks should be shown. Typically a
     * {@link Skin} implementation will only show labels if
     * {@link #showTickMarksProperty() showTickMarks} is also true.
     *
     * @return A BooleanProperty
     */
    public final BooleanProperty showTickLabelsProperty() {
        if (showTickLabels == null) {
            showTickLabels = new StyleableBooleanProperty(false) {
                @Override
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return MultiIndexSlider.StyleableProperties.SHOW_TICK_LABELS;
                }

                @Override
                public Object getBean() {
                    return MultiIndexSlider.this;
                }

                @Override
                public String getName() {
                    return "showTickLabels"; //$NON-NLS-1$
                }
            };
        }
        return showTickLabels;
    }

    /**
     *
     */
    private BooleanProperty showTickMarks;

    /**
     * Specifies whether the {@link Skin} implementation should show tick marks.
     *
     * @param value
     */
    public final void setShowTickMarks(boolean value) {
        showTickMarksProperty().set(value);
    }

    /**
     * @return whether the {@link Skin} implementation should show tick marks.
     */
    public final boolean isShowTickMarks() {
        return showTickMarks == null ? false : showTickMarks.get();
    }

    /**
     * @return A BooleanProperty that specifies whether the {@link Skin}
     * implementation should show tick marks.
     */
    public final BooleanProperty showTickMarksProperty() {
        if (showTickMarks == null) {
            showTickMarks = new StyleableBooleanProperty(false) {
                @Override
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return MultiIndexSlider.StyleableProperties.SHOW_TICK_MARKS;
                }

                @Override
                public Object getBean() {
                    return MultiIndexSlider.this;
                }

                @Override
                public String getName() {
                    return "showTickMarks"; //$NON-NLS-1$
                }
            };
        }
        return showTickMarks;
    }


    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/

    /**
     * Ensures that min is always < max, that value is always
     * somewhere between the two, and that if snapToTicks is set then the
     * value will always be set to align with a tick mark.
     */
    private void adjustValues() {
        adjustLowValues();
        adjustHighValues();
    }

    private void adjustLowValues() {
        /**
         * We first look if the LowValue is between the min and max.
         */
        if (getLowValue() < getMin() || getLowValue() > getMax()) {
            double value = Utils.clamp(getMin(), getLowValue(), getMax());
            setLowValue(value);
            /**
             * If the LowValue seems right, we check if it's not superior to
             * HighValue ONLY if the highValue itself is right. Because it may
             * happen that the highValue has not yet been computed and is
             * wrong, and therefore force the lowValue to change in a wrong way
             * which may end up in an infinite loop.
             */
        } else if (getLowValue() >= getHighValue() && (getHighValue() >= getMin() && getHighValue() <= getMax())) {
            double value = Utils.clamp(getMin(), getLowValue(), getHighValue());
            setLowValue(value);
        }
    }

    private double snapValueToTicks(double d) {
        double d1 = d;
        if (isSnapToTicks()) {
            double d2 = 0.0D;
            if (getMinorTickCount() != 0) {
                d2 = getMajorTickUnit() / (double) (Math.max(getMinorTickCount(), 0) + 1);
            } else {
                d2 = getMajorTickUnit();
            }
            int i = (int) ((d1 - getMin()) / d2);
            double d3 = (double) i * d2 + getMin();
            double d4 = (double) (i + 1) * d2 + getMin();
            d1 = Utils.nearest(d3, d1, d4);
        }
        return Utils.clamp(getMin(), d1, getMax());
    }

    private void adjustHighValues() {
        if (getHighValue() < getMin() || getHighValue() > getMax()) {
            setHighValue(Utils.clamp(getMin(), getHighValue(), getMax()));
        } else if (getHighValue() < getLowValue() && (getLowValue() >= getMin() && getLowValue() <= getMax())) {
            setHighValue(Utils.clamp(getLowValue(), getHighValue(), getMax()));
        }
    }


    /**************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "range-slider"; //$NON-NLS-1$

    private static class StyleableProperties {
        private static final CssMetaData<MultiIndexSlider, Number> BLOCK_INCREMENT =
            new CssMetaData<MultiIndexSlider, Number>("-fx-block-increment", //$NON-NLS-1$
                SizeConverter.getInstance(), 10.0) {

                @Override
                public boolean isSettable(MultiIndexSlider n) {
                    return n.blockIncrement == null || !n.blockIncrement.isBound();
                }

                @SuppressWarnings("unchecked")
                @Override
                public StyleableProperty<Number> getStyleableProperty(MultiIndexSlider n) {
                    return (StyleableProperty<Number>) n.blockIncrementProperty();
                }
            };

        private static final CssMetaData<MultiIndexSlider, Boolean> SHOW_TICK_LABELS =
            new CssMetaData<MultiIndexSlider, Boolean>("-fx-show-tick-labels", //$NON-NLS-1$
                BooleanConverter.getInstance(), Boolean.FALSE) {

                @Override
                public boolean isSettable(MultiIndexSlider n) {
                    return n.showTickLabels == null || !n.showTickLabels.isBound();
                }

                @SuppressWarnings("unchecked")
                @Override
                public StyleableProperty<Boolean> getStyleableProperty(MultiIndexSlider n) {
                    return (StyleableProperty<Boolean>) n.showTickLabelsProperty();
                }
            };

        private static final CssMetaData<MultiIndexSlider, Boolean> SHOW_TICK_MARKS =
            new CssMetaData<MultiIndexSlider, Boolean>("-fx-show-tick-marks", //$NON-NLS-1$
                BooleanConverter.getInstance(), Boolean.FALSE) {

                @Override
                public boolean isSettable(MultiIndexSlider n) {
                    return n.showTickMarks == null || !n.showTickMarks.isBound();
                }

                @SuppressWarnings("unchecked")
                @Override
                public StyleableProperty<Boolean> getStyleableProperty(MultiIndexSlider n) {
                    return (StyleableProperty<Boolean>) n.showTickMarksProperty();
                }
            };

        private static final CssMetaData<MultiIndexSlider, Boolean> SNAP_TO_TICKS =
            new CssMetaData<MultiIndexSlider, Boolean>("-fx-snap-to-ticks", //$NON-NLS-1$
                BooleanConverter.getInstance(), Boolean.FALSE) {

                @Override
                public boolean isSettable(MultiIndexSlider n) {
                    return n.snapToTicks == null || !n.snapToTicks.isBound();
                }

                @SuppressWarnings("unchecked")
                @Override
                public StyleableProperty<Boolean> getStyleableProperty(MultiIndexSlider n) {
                    return (StyleableProperty<Boolean>) n.snapToTicksProperty();
                }
            };

        private static final CssMetaData<MultiIndexSlider, Number> MAJOR_TICK_UNIT =
            new CssMetaData<MultiIndexSlider, Number>("-fx-major-tick-unit", //$NON-NLS-1$
                SizeConverter.getInstance(), 25.0) {

                @Override
                public boolean isSettable(MultiIndexSlider n) {
                    return n.majorTickUnit == null || !n.majorTickUnit.isBound();
                }

                @SuppressWarnings("unchecked")
                @Override
                public StyleableProperty<Number> getStyleableProperty(MultiIndexSlider n) {
                    return (StyleableProperty<Number>) n.majorTickUnitProperty();
                }
            };

        private static final CssMetaData<MultiIndexSlider, Number> MINOR_TICK_COUNT =
            new CssMetaData<MultiIndexSlider, Number>("-fx-minor-tick-count", //$NON-NLS-1$
                SizeConverter.getInstance(), 3.0) {

                @SuppressWarnings("deprecation")
                @Override
                public void set(MultiIndexSlider node, Number value, StyleOrigin origin) {
                    super.set(node, value.intValue(), origin);
                }

                @Override
                public boolean isSettable(MultiIndexSlider n) {
                    return n.minorTickCount == null || !n.minorTickCount.isBound();
                }

                @SuppressWarnings("unchecked")
                @Override
                public StyleableProperty<Number> getStyleableProperty(MultiIndexSlider n) {
                    return (StyleableProperty<Number>) n.minorTickCountProperty();
                }
            };

        private static final CssMetaData<MultiIndexSlider, Orientation> ORIENTATION =
            new CssMetaData<MultiIndexSlider, Orientation>("-fx-orientation", //$NON-NLS-1$
                new EnumConverter<>(Orientation.class),
                Orientation.HORIZONTAL) {

                @Override
                public Orientation getInitialValue(MultiIndexSlider node) {
                    // A vertical Slider should remain vertical
                    return node.getOrientation();
                }

                @Override
                public boolean isSettable(MultiIndexSlider n) {
                    return n.orientation == null || !n.orientation.isBound();
                }

                @SuppressWarnings("unchecked")
                @Override
                public StyleableProperty<Orientation> getStyleableProperty(MultiIndexSlider n) {
                    return (StyleableProperty<Orientation>) n.orientationProperty();
                }
            };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                new ArrayList<>(Control.getClassCssMetaData());
            styleables.add(BLOCK_INCREMENT);
            styleables.add(SHOW_TICK_LABELS);
            styleables.add(SHOW_TICK_MARKS);
            styleables.add(SNAP_TO_TICKS);
            styleables.add(MAJOR_TICK_UNIT);
            styleables.add(MINOR_TICK_COUNT);
            styleables.add(ORIENTATION);

            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    /**
     * @return The CssMetaData associated with this class, which may include the
     * CssMetaData of its super classes.
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE =
        PseudoClass.getPseudoClass("vertical"); //$NON-NLS-1$
    private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE =
        PseudoClass.getPseudoClass("horizontal"); //$NON-NLS-1$
}
