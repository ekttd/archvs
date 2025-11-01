package cpu.interrupts.exceptions;

public class RegisterAddressOutOfBoundsException extends InterruptException {
    public RegisterAddressOutOfBoundsException(String errorMessage) {
        super(4, false, errorMessage);
    }
}
