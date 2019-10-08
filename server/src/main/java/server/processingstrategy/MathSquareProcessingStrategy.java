package server.processingstrategy;

public class MathSquareProcessingStrategy implements ProcessingStrategy {

    @Override
    public String process(String request) {
        try {
            int i = Integer.parseInt(request);
            return String.valueOf(i * i);
        } catch (NumberFormatException e) {
            return e.toString();
        }

    }

}
