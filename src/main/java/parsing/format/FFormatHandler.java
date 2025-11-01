package parsing.format;

import cpu.Flag;
import instructions.Instruction;
import operands.OperandsF;
import parsing.IncorrectFormatException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FFormatHandler extends FormatHandler {
    public FFormatHandler() {
        super(OperandsF.class);
    }

    @Override
    protected List<String> createOperandRegexes() {
        List<String> operandRegexes = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;

        for (var flag : Flag.values()) {
            if (!isFirst)
                builder.append("|");

            builder
                    .append(flag.getName().toLowerCase())
                    .append("|")
                    .append(flag.getName().toUpperCase());

            isFirst = false;
        }

        operandRegexes.add(builder.toString());
        return operandRegexes;
    }

    @Override
    public int handleRequest(String request) throws IncorrectFormatException {
        int instructionData = 0;
        if (Pattern.compile(formatRegex).matcher(request).matches()) {
            String[] components = request.trim().split("( +)");
            int offset = Instruction.SIZE;
            instructionData = Instruction.fromMnemonic(components[0]).getOpcode() << (offset -= Instruction.OPCODE_SIZE);
            instructionData |= Flag.fromName(components[1]).getValue() << (offset -= Flag.SIZE);
        } else if (successor != null) {
            instructionData = successor.handleRequest(request);
        } else {
            throw new IncorrectFormatException("Неверный формат");
        }
        return instructionData;
    }
}
