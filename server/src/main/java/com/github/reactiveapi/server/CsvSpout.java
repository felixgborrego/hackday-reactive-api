package com.github.reactiveapi.server;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

/**
 * Created by felixgarcia on 04/04/2014.
 */
public class CsvSpout extends BaseRichSpout {

    LineIterator itLine;
    private SpoutOutputCollector collector;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields(FieldsNames.HOUSE_LINE));
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        InputStream input = this.getClass().getResourceAsStream("/house.csv");
        try {
            itLine = IOUtils.lineIterator(input, "UTF-8");
            itLine.nextLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        collector = spoutOutputCollector;
    }

    @Override
    public void nextTuple() {
        if (itLine.hasNext()) {
            String line = itLine.nextLine();
            this.collector.emit(new Values(line));
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
