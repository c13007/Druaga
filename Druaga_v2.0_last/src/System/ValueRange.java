package System;

public class ValueRange {

    /**
     * 指定した範囲内に数値を収める
     *
     * @param value　修正する数値
     * @param min　最小値
     * @param max　最大値
     * @return 修正した数値
     */
    public static int rangeOf(int value, int min, int max) {
        if(min > max)   return max;
        if(min > value) return min;
        if(value > max) return max;
        return value;
    }
}
