package com.github.reactiveapi.server;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

/**
 * Hello world!
 */
public class App {
    private static final String LINE_SPOUT_ID = "sentence-spout";
    private static final String JSON_BOLT_ID = "json-bolt";
    private static final String PUBLISH_BOLT_ID = "publish-bolt";
    private static final String TOPOLOGY_NAME = "push-topology";

    public static void main(String[] args) {
        System.out.println("Hello World!");


        CsvSpout spout = new CsvSpout();
        ToJsonBolt jsonBolt = new ToJsonBolt();
        PushNotificationBolt pushBolt = new PushNotificationBolt();


        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout(LINE_SPOUT_ID, spout);

        // SentenceSpout --> SplitSentenceBolt
        builder.setBolt(JSON_BOLT_ID, jsonBolt)
                .shuffleGrouping(LINE_SPOUT_ID);


        // Json bold --> Publish bold
        builder.setBolt(PUBLISH_BOLT_ID, pushBolt)
                .shuffleGrouping(JSON_BOLT_ID);//, new Fields(FieldsNames.HOUSE_JSON));


        Config config = new Config();
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(TOPOLOGY_NAME, config, builder.
                createTopology());
        waitForSeconds(10);
        cluster.killTopology(TOPOLOGY_NAME);
        cluster.shutdown();


    }

    private static void waitForSeconds(int i) {
        try {
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
