package com.github.reactiveapi.server;

import com.google.gson.Gson;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import static com.github.reactiveapi.server.FieldsNames.HOUSE_JSON;

/**
 * Created by felixgarcia on 04/04/2014.
 */
public class ToJsonBolt extends BaseRichBolt {
    private OutputCollector collector;
    private static String SEPARATOR = ",";
    Gson gson;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        collector = outputCollector;
        gson = new Gson();
    }

    @Override
    public void execute(Tuple tuple) {
        String line = tuple.getStringByField(FieldsNames.HOUSE_LINE);
        String[] rows = line.split(SEPARATOR);

        String address = rows[2];
        String area = rows[4];
        House house = new House(address, area);

        String json = gson.toJson(house);

        this.collector.emit(new Values(json));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(HOUSE_JSON));
    }

    private class House {
        String address;
        String area;

        public House(String address, String area) {
            this.address = address;
            this.area = area;
        }
    }
}
