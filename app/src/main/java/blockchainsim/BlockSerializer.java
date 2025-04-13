package blockchainsim;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class BlockSerializer extends StdSerializer<Block> {

    public BlockSerializer(){
        this(null);
    }

    public BlockSerializer(Class<Block> t){
        super(t);
    }

    @Override
    public void serialize(Block block, JsonGenerator jsonGen, SerializerProvider serializerProvider) throws IOException {

        jsonGen.writeStartObject(); // BLOCK START

        jsonGen.writeArrayFieldStart("header"); // HEADER START
        jsonGen.writeStartObject();
        jsonGen.writeNumberField("index" , block.getIndex());
        jsonGen.writeNumberField("timestamp", block.getTimestamp());
        jsonGen.writeStringField("previousHash", block.getPreviousHash());
        jsonGen.writeStringField("Hash", block.getHash());
        jsonGen.writeEndObject();
        jsonGen.writeEndArray(); // HEADER END

        jsonGen.writeArrayFieldStart("data"); // DATA START

        for(Transaction tx: block.getData()){
            jsonGen.writeStartObject();
            jsonGen.writeObjectFieldStart(tx.getTransactionID()); // TRANSACTION START

            if(tx instanceof PeerTransaction){ // IF PEER TX, INCLUDE INPUTS

                jsonGen.writeArrayFieldStart("inputs"); // INPUTS ARRAY START
                for(int i = 0; i < ((PeerTransaction) tx).getInputs().size(); i = i+2){
                    jsonGen.writeStartObject(); // INPUT OBJ START
                    jsonGen.writeObjectField("txid", ((PeerTransaction) tx).getInputs().get(i));
                    jsonGen.writeObjectField("outputIndex", ((PeerTransaction) tx).getInputs().get(i+1));
                    jsonGen.writeEndObject(); // INPUT OBJ END
                }
                jsonGen.writeEndArray(); // INPUTS ARRAY END
            }

            jsonGen.writeArrayFieldStart("outputs");// OUTPUTS ARRAY START
            for(int i = 0; i < tx.getOutputs().size(); i = i+2){
                jsonGen.writeStartObject(); // OUTPUT OBJ START
                jsonGen.writeObjectField("value",  tx.getOutputs().get(i));
                jsonGen.writeObjectField("recipient", tx.getOutputs().get(i+1));
                jsonGen.writeEndObject(); // OUTPUT OBJ END
            }

            jsonGen.writeEndArray(); // OUTPUTS ARRAY END
            jsonGen.writeEndObject(); // TRANSACTION END
            jsonGen.writeEndObject();

        }

        jsonGen.writeEndArray(); // DATA END

        jsonGen.writeEndObject(); // BLOCK END
    }
}
