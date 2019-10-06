package server;

class MathSquareProcessingStrategy implements IProcessingStrategy {

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
