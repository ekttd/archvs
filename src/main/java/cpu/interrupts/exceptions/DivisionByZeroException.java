package cpu.interrupts.exceptions;

public class DivisionByZeroException extends InterruptException {
    public DivisionByZeroException(String errorMessage) {
        super(0, false, errorMessage);
    }
}
