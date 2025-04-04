package blockchainsim;

public class FaultyNode extends Node{
        public FaultyNode(Blockchain blockchain) {
            super(blockchain);
        }

        @Override
        public boolean verifyTransaction(Transaction tx){
            return false; // Returns false for testing the consenus algorithm
        }
    }

