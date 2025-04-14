package blockchainsim;


import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class BlockUtil {

    private BlockUtil() {
    }

    // Method to format all data from all transactions
    public static String formatData(ArrayList<Transaction> data){
        StringBuilder dataStr = new StringBuilder();
        for(Transaction tx: data){
            dataStr.append(tx.getTransactionID()).append(tx.getReceiver()).append(tx.getTimestamp()).append(tx.getAmount()).append(tx.getOutputs());
            if(tx instanceof PeerTransaction){
                dataStr.append(dataStr.append(((PeerTransaction)tx).getInputs()).append(((PeerTransaction)tx).getSender()));
            }
        }
        System.out.println(dataStr);
        return dataStr.toString();
    }

    public static void storeBlock(Block block) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        SimpleModule module = new SimpleModule();
        module.addSerializer(Block.class, new BlockSerializer());
        mapper.registerModule(module);


        try{
            mapper.writeValue(new File("app/src/main/resources/BlockData/block000000000" + block.getIndex() + ".json"), block);

        } catch(IOException e){
            throw(new RuntimeException(e));
        }


    }

    public static Block readBlock(File file)  {

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(file);
            JsonNode header = root.get("header").get(0);
            Block block = new Block(header.get("index"),header.get("timestamp"),header.get("previousHash"),header.get("hash"),header.get("nonce"),header.get("difficulty"));
            JsonNode data = root.get("data");
            for(int i = 0; i < data.size(); i++){
                JsonNode txNode = data.get(i);
                String txId = txNode.fieldNames().next();
                JsonNode txData = txNode.get(txId);

                if(i == 0){ // First transaction in list always a coinbase transaction
                    CoinbaseTransaction cbtx = new CoinbaseTransaction();
                    cbtx.setTransactionID(txId);
                    cbtx.setTimestamp(txData.get("timestamp").asLong());

                    JsonNode output = txData.get("outputs").get(0); // only has 1 output (reward to miner)
                    System.out.println(output.get("value").asDouble() + " " + output.get("recipient").asText());
                    cbtx.addOutput(output.get("value").asDouble(), output.get("recipient").asText());
                    cbtx.setAmount(output.get("value").asDouble());
                    cbtx.setReceiver(output.get("recipient").asText());

                    block.addTransaction(cbtx);
                } else {
                    PeerTransaction ptx = new PeerTransaction();
                    ptx.setTransactionID(txId);
                    ptx.setTimestamp(txData.get("timestamp").asLong());
                    ptx.setSender(txData.get("sender").asText());
                    ptx.setReceiver(txData.get("recipient").asText());
                    ptx.setAmount(txData.get("amount").asDouble());

                    JsonNode inputs = txData.get("inputs");
                    for (JsonNode input : inputs) {
                        ptx.addInput(input.get("txid").asText(), input.get("outputIndex").asInt());
                    }

                    JsonNode outputs = txData.get("outputs");
                    for (JsonNode output : outputs) {
                        ptx.addOutput(output.get("value").asDouble(), output.get("recipient").asText());
                    }


                    block.addTransaction(ptx);
                }
            }
            return block;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}

