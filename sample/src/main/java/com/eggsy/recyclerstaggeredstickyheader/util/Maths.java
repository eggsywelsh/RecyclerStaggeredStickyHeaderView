package com.eggsy.recyclerstaggeredstickyheader.util;

import java.util.ArrayList;

/**
 * Created by eggsy on 17-2-21.
 */

public final class Maths {

    /**
     * use the binary search method finds the closest title position after the current position
     *
     * @param arrayTitlePos      search array
     * @param coordinatePosition coordinate position
     * @return
     */
    public static int findRecentMaxValue(ArrayList<Integer> arrayTitlePos, int coordinatePosition) {
        int resultPos = Integer.MIN_VALUE;
        if (arrayTitlePos == null) {
            return resultPos;
        }
        int low = 0, high = arrayTitlePos.size() - 1;
        while (high - low >= 0) {
            int halfPosition = low + ((high - low) >> 1);
            if (arrayTitlePos.get(halfPosition) < coordinatePosition) {
                low = halfPosition + 1;

                if (low < arrayTitlePos.size() && arrayTitlePos.get(low) > coordinatePosition) {
                    resultPos = low;
                    break;
                }
            } else if (arrayTitlePos.get(halfPosition) > coordinatePosition) {
                if (halfPosition - 1 < 0) {
                    resultPos = halfPosition;
                    break;
                } else {
                    if (arrayTitlePos.get(halfPosition - 1) < coordinatePosition) {
                        resultPos = halfPosition;
                        break;
                    } else {
                        high = halfPosition - 1;
                    }
                }
            } else {
                resultPos = halfPosition;
                break;
            }
        }
        return resultPos >= 0 && resultPos < arrayTitlePos.size() ? arrayTitlePos.get(resultPos) : resultPos;
    }

    /**
     * use the binary search method find the closest title position before the current position
     *
     * @param arrayTitlePos      search array
     * @param coordinatePosition coordinate position
     * @return
     */
    public static int findRecentMinValue(ArrayList<Integer> arrayTitlePos, int coordinatePosition) {
        int resultPos = Integer.MIN_VALUE;
        if (arrayTitlePos == null) {
            return resultPos;
        }
        int low = 0, high = arrayTitlePos.size() - 1;
        while (high - low >= 0) {
            int halfPosition = low + ((high - low) >> 1);
            if (arrayTitlePos.get(halfPosition) < coordinatePosition) {
                if ((halfPosition + 1) < arrayTitlePos.size()) {
                    if (arrayTitlePos.get(halfPosition + 1) > coordinatePosition) {
                        resultPos = halfPosition;
                        break;
                    } else {
                        low = halfPosition + 1;
                    }
                } else {
                    resultPos = halfPosition;
                    break;
                }
            } else if (arrayTitlePos.get(halfPosition) > coordinatePosition) {
                if (halfPosition - 1 < 0) {
                    break;
                } else {
                    if (arrayTitlePos.get(halfPosition-1) < coordinatePosition) {
                        resultPos = halfPosition - 1;
                        break;
                    } else {
                        high = halfPosition - 1;
                    }
                }
            } else {
                resultPos = halfPosition;
                break;
            }
        }
        return resultPos >= 0 && resultPos < arrayTitlePos.size() ? arrayTitlePos.get(resultPos) : resultPos;
    }

}
