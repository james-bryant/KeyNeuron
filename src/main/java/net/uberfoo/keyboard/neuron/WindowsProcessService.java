package net.uberfoo.keyboard.neuron;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.*;
import com.sun.jna.platform.win32.*;
import net.uberfoo.keyboard.neuron.model.ProcessInfo;

public class WindowsProcessService extends AbstractProcessService {
    public interface User32 extends StdCallLibrary {
        User32 INSTANCE = Native.load("user32", User32.class);
        WinDef.HWND GetForegroundWindow();
        int GetWindowTextW(WinDef.HWND hWnd, char[] lpString, int nMaxCount);
        int GetWindowThreadProcessId(WinDef.HWND hWnd, IntByReference lpdwProcessId);
    }

    public interface Psapi extends StdCallLibrary {
        Psapi INSTANCE = Native.load("psapi", Psapi.class);
        int GetModuleBaseNameW(WinNT.HANDLE hProcess, Pointer hModule, char[] lpBaseName, int nSize);
    }

    public interface Kernel32 extends StdCallLibrary {
        Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class);
        WinNT.HANDLE OpenProcess(int dwDesiredAccess, boolean bInheritHandle, int dwProcessId);
        boolean CloseHandle(WinNT.HANDLE hObject);
    }

    @Override
    protected Runnable getRunner() {
        return new Runnable() {
            String lastProcess = "";
            IntByReference lastPid = new IntByReference();

            @Override
            public void run() {
                WinDef.HWND foregroundWindow = User32.INSTANCE.GetForegroundWindow();
                if (foregroundWindow != null) {
                    char[] windowText = new char[512];
                    User32.INSTANCE.GetWindowTextW(foregroundWindow, windowText, 512);
                    String wText = Native.toString(windowText).trim();
                    if (!wText.isEmpty()) {
                        IntByReference pid = new IntByReference();
                        User32.INSTANCE.GetWindowThreadProcessId(foregroundWindow, pid);
                        if (pid.getValue() == lastPid.getValue()) {
                            return;
                        }
                        lastPid = pid;
                        WinNT.HANDLE process = Kernel32.INSTANCE.OpenProcess(WinNT.PROCESS_QUERY_INFORMATION | WinNT.PROCESS_VM_READ, false, pid.getValue());
                        char[] exeName = new char[512];
                        Psapi.INSTANCE.GetModuleBaseNameW(process, Pointer.NULL, exeName, 512);
                        String processName = Native.toString(exeName).trim();
                        Kernel32.INSTANCE.CloseHandle(process);
                        if (!processName.equals(lastProcess)) {
                            lastProcess = processName;
                            var processInfo = new ProcessInfo(processName, wText, pid.getValue());
                            notifyConsumer(processInfo);
                        }
                    }
                }
            }
        };
    }
}
