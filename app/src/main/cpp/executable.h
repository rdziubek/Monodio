#ifndef MY_APPLICATION_EXECUTABLE_H
#define MY_APPLICATION_EXECUTABLE_H

struct Executable {
    static inline const char *kPathPrivileged = "/sbin/su";
    static inline const char *kPathDefault = "/system/bin/sh";
    static inline const char *kAliasPrivileged = "su";
    static inline const char *kAliasDefault = "sh";
};

#endif //MY_APPLICATION_EXECUTABLE_H
