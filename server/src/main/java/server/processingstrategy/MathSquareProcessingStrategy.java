package server.processingstrategy;

public class MathSquareProcessingStrategy implements ProcessingStrategy {

    @Override
    public String process(String request) throws ProcessingException {
        try {
            int i = Integer.parseInt(request);
            return String.valueOf(i * i);
        } catch (NumberFormatException e) {
            throw new ProcessingException(e, 1);
        }
    }

}
