package server.processingstrategy;

public class PrimeDecomposeProcessingStrategy implements ProcessingStrategy {

    @Override
    public String process(String request) throws ProcessingException {
        try {
            return PrimeSieve.decompose(Integer.parseInt(request));
        } catch (NumberFormatException e) {
            throw new ProcessingException("Wrong argument: integer number expected", 1);
        }
    }

}
