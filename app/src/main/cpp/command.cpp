#include "command.h"
#include "executable.h"

#include <jni.h>
#include <string>
#include <unistd.h>
#include <android/log.h>
#include <bitset>
#include <iomanip>
#include <array>
#include <sys/wait.h>

Command::Command(const std::string &command) {
    this->command_ = command;
}

void Command::execute(User as) {
    constexpr int kReadEnd = 0;
    constexpr int kWriteEnd = 1;

    int in_file_descriptor[2] = {0, 0};
    int out_file_descriptor[2] = {0, 0};
    int error_file_descriptor[2] = {0, 0};

    auto cleanup = [&]() {
        ::close(in_file_descriptor[kReadEnd]);
        ::close(in_file_descriptor[kWriteEnd]);

        ::close(out_file_descriptor[kReadEnd]);
        ::close(out_file_descriptor[kWriteEnd]);

        ::close(error_file_descriptor[kReadEnd]);
        ::close(error_file_descriptor[kWriteEnd]);
    };

    auto rc = ::pipe(in_file_descriptor);
    if (rc < 0) {
        throw std::runtime_error(std::strerror(errno));
    }

    rc = ::pipe(out_file_descriptor);
    if (rc < 0) {
        ::close(in_file_descriptor[kReadEnd]);
        ::close(in_file_descriptor[kWriteEnd]);
        throw std::runtime_error(std::strerror(errno));
    }

    rc = ::pipe(error_file_descriptor);
    if (rc < 0) {
        ::close(in_file_descriptor[kReadEnd]);
        ::close(in_file_descriptor[kWriteEnd]);

        ::close(out_file_descriptor[kReadEnd]);
        ::close(out_file_descriptor[kWriteEnd]);
        throw std::runtime_error(std::strerror(errno));
    }

    auto pid = fork();
    if (pid > 0) {  // When spawned process is the parent process.
        ::close(in_file_descriptor[kReadEnd]);
        ::close(out_file_descriptor[kWriteEnd]);
        ::close(error_file_descriptor[kWriteEnd]);

        if (::write(in_file_descriptor[kWriteEnd],
                    std_in_.data(),
                    std_in_.size()) < 0) {
            throw std::runtime_error(std::strerror(errno));
        }
        ::close(in_file_descriptor[kWriteEnd]);
    } else if (pid == 0) {  // When spawned process is the child process.
        ::dup2(in_file_descriptor[kReadEnd], STDIN_FILENO);
        ::dup2(out_file_descriptor[kWriteEnd], STDOUT_FILENO);
        ::dup2(error_file_descriptor[kWriteEnd], STDERR_FILENO);

        ::close(in_file_descriptor[kWriteEnd]);
        ::close(out_file_descriptor[kReadEnd]);
        ::close(error_file_descriptor[kReadEnd]);


        ::execl(as == User::kDefault ? Executable::kPathDefault : Executable::kPathPrivileged,
                as == User::kDefault ? Executable::kAliasDefault : Executable::kAliasPrivileged,
                "-c",
                command_.c_str(), nullptr);
        ::exit(EXIT_SUCCESS);
    }

    if (pid < 0) {  // No process (or an invalid one) has been allocated.
        cleanup();
        throw std::runtime_error("Failed to fork");
    }

    int status = 0;
    ::waitpid(pid, &status, 0);

    std::array<char, 256> buffer{};

    ssize_t bytes = 0;
    do {
        bytes = ::read(out_file_descriptor[kReadEnd],
                       buffer.data(),
                       buffer.size());
        std_out_.append(buffer.data(), bytes);
    } while (bytes > 0);

    do {
        bytes = ::read(error_file_descriptor[kReadEnd],
                       buffer.data(),
                       buffer.size());
        std_err_.append(buffer.data(), bytes);
    } while (bytes > 0);

    if (WIFEXITED(status)) {
        exit_status_ = WEXITSTATUS(status);
    }

    cleanup();
}
