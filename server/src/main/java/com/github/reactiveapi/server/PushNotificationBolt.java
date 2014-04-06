package com.github.reactiveapi.server;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

/**
 * Created by felixgarcia on 04/04/2014.
 */
public class PushNotificationBolt extends BaseRichBolt {

    GCMClient gcmClient;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        gcmClient = new GCMClient();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        // this bolt does not emit
    }

    @Override
    public void execute(Tuple tuple) {
        String json = tuple.getStringByField(FieldsNames.HOUSE_JSON);
        push(json);
    }

    private void push(String json) {
        gcmClient.push(json);
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }
}
