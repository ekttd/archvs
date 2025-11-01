package cpu.interrupts.exceptions;

public class UnknownInstructionException extends InterruptException {
    public UnknownInstructionException(String errorMessage) {
        super(1, false, errorMessage);
    }
}
