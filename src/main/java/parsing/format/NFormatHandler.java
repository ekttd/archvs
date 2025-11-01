package parsing.format;

import instructions.Instruction;
import operands.OperandsN;
import parsing.IncorrectFormatException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class NFormatHandler extends FormatHandler {
    public NFormatHandler() {
        //super(Instruction.OperandsFormat.N);
        super(OperandsN.class);
    }

    @Override
    protected List<String> createOperandRegexes() {
        return new ArrayList<>();
    }

    @Override
    public int handleRequest(String request) throws IncorrectFormatException {
        int instructionData = 0;
        if (Pattern.compile(formatRegex).matcher(request).matches()) {
            instructionData = Instruction.fromMnemonic(request.trim()).getOpcode() << (Instruction.SIZE - Instruction.OPCODE_SIZE);
        } else if (successor != null) {
            instructionData = successor.handleRequest(request);
        } else {
            throw new IncorrectFormatException("Неверный формат");
        }
        return instructionData;
    }

}
