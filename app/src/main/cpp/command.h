#ifndef MONODIO_COMMAND_H
#define MONODIO_COMMAND_H

#include <string>
#include "user.h"

class Command {
public:
    explicit Command(const std::string &command);

    void execute(User as = User::kPrivileged);

    [[nodiscard]] auto command() const -> const std::string & { return command_; }

    [[nodiscard]] auto exit_status() const -> const int & { return exit_status_; }

    [[nodiscard]] auto std_out() const -> const std::string & { return std_out_; }

    [[nodiscard]] auto std_err() const -> const std::string & { return std_err_; }

    auto std_in() -> std::string & { return std_in_; }

private:
    std::string command_;
    int exit_status_ = 0;
    std::string std_in_;
    std::string std_out_;
    std::string std_err_;
};

#endif //MONODIO_COMMAND_H
