package cpu.interrupts.exceptions;

public class MemoryAddressOutOfBoundsException extends InterruptException {
    public MemoryAddressOutOfBoundsException(String errorMessage) {
        super(3, false, errorMessage);
    }
}
