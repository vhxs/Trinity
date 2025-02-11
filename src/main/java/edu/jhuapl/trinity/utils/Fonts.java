package edu.jhuapl.trinity.utils;

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

import javafx.scene.text.Font;


public class Fonts {
    private static final String ROBOTO_BLACK;
    private static String robotoBlackName;

    private static final String ROBOTO_REGULAR;
    private static String robotoRegularName;

    private static final String ROBOTO_THIN;
    private static String robotoThinName;


    static {
        try {
            robotoBlackName = Font.loadFont(Fonts.class.getResourceAsStream("/edu/jhuapl/trinity/css/Roboto-Black.ttf"), 16).getName();
        } catch (Exception exception) {
        }
        try {
            robotoRegularName = Font.loadFont(Fonts.class.getResourceAsStream("/edu/jhuapl/trinity/css/Roboto-Regular.ttf"), 14).getName();
        } catch (Exception exception) {
        }
        try {
            robotoThinName = Font.loadFont(Fonts.class.getResourceAsStream("/edu/jhuapl/trinity/css/Roboto-Thin.ttf"), 12).getName();
        } catch (Exception exception) {
        }

        ROBOTO_BLACK = robotoBlackName;
        ROBOTO_REGULAR = robotoRegularName;
        ROBOTO_THIN = robotoThinName;
    }

    public static Font robotoBlack(final double size) {
        return new Font(ROBOTO_BLACK, size);
    }

    public static Font robotoRegular(final double size) {
        return new Font(ROBOTO_REGULAR, size);
    }

    public static Font robotoThin(final double size) {
        return new Font(ROBOTO_THIN, size);
    }
}
