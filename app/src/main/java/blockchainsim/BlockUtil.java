package blockchainsim;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class BlockUtil {

    private BlockUtil() {
    }

    public static void storeBlock(Block block) {

        ObjectMapper mapper = new ObjectMapper();

        try{
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("app/src/main/resources/BlockData/block000000" + block.getIndex() + ".json"), block);
        } catch (IOException e) {
            throw new RuntimeException(e);
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

}

