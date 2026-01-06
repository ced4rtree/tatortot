package org.teamtators.tester.components;

import java.util.List;
import java.util.function.Consumer;
import org.teamtators.tester.ManualTest;
import org.teamtators.util.EnumUtils;
import org.teamtators.util.XBOXController;

public class EnumSwitchTest<T extends Enum<T>> extends ManualTest {
    private int enumIndex = 0;
    private List<String> enumValueNames;
    private Class<T> enumType;
    private Consumer<T> setStateCallback;

    public EnumSwitchTest(String name, Class<T> enumType, Consumer<T> setStateCallback) {
        super(name);
        this.enumType = enumType;
        this.enumValueNames = EnumUtils.enum2nameslist(enumType);
        this.setStateCallback = setStateCallback;
    }

    @Override
    public void start() {
        printTestInstructions("Press A to set value, Left and right bumpers to switch values");
        enumIndex = 0;
    }

    @Override
    public void onButtonDown(XBOXController.Button button) {
        switch (button) {
            case kA:
                printTestInfo("Value set to " + enumValueNames.get(enumIndex));
                setStateCallback.accept(EnumUtils.name2const(enumType, enumValueNames.get(enumIndex)));
                break;
            case kBUMPER_LEFT:
                if (enumIndex == 0) {
                    enumIndex = enumValueNames.size() - 1;
                } else {
                    enumIndex -= 1;
                }
                printTestInfo("Selecting " + enumValueNames.get(enumIndex));
                break;
            case kBUMPER_RIGHT:
                if (enumIndex == enumValueNames.size() - 1) {
                    enumIndex = 0;
                } else {
                    enumIndex += 1;
                }
                printTestInfo("Selecting " + enumValueNames.get(enumIndex));
                break;
            default:
                printTestInfo("button without a binding pressed");
                break;
        }
    }
}
