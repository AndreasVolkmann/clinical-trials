package me.avo.clinical.trials.smile;

import smile.data.AttributeDataset;

public class Trainer {

    private AttributeDataset train;
    private AttributeDataset test;

    Trainer(AttributeDataset train, AttributeDataset test) {
        this.train = train;
        this.test = test;
    }

    public double[][] x;
    public int[] y;
    public double[][] testX;
    public int[] testY;

    public void setup() {
        x = train.toArray(new double[train.size()][]);
        y = train.toArray(new int[train.size()]);
        testX = test.toArray(new double[test.size()][]);
        testY = test.toArray(new int[test.size()]);
    }

}
