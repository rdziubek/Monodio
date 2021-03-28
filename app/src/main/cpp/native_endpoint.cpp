#include <jni.h>
#include <string>
#include <unistd.h>
#include <android/log.h>
#include <bitset>
#include <iomanip>
#include <array>
#include <sys/wait.h>
#include "command.h"

extern "C" JNIEXPORT jobject JNICALL
Java_pl_witampanstwa_monodio_MainActivity_execute(
        JNIEnv *env,
        jobject that,
        jstring arguments,
        jint user) {

    Command command(env->GetStringUTFChars(arguments, nullptr));
    command.execute(static_cast<User>(user));

    jclass commandClassModel = env->FindClass("pl/witampanstwa/monodio/component/CommandResult");
    jmethodID methodId = env->GetMethodID(commandClassModel, "<init>",
                                          "(ILjava/lang/String;Ljava/lang/String;)V");
    jobject commandInstanceModel = env->NewObject(commandClassModel, methodId,
                                                  static_cast<jint>(command.exit_status()),
                                                  env->NewStringUTF(command.std_out().c_str()),
                                                  env->NewStringUTF(command.std_err().c_str()));

    return commandInstanceModel;
}
