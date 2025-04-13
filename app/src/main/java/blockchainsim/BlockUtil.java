package blockchainsim;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.IOException;

public class BlockUtil {

    private BlockUtil() {
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

    public static Block readBlock(String path)  {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(path), Block.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Block readBlock(File file)  {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(file, Block.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

