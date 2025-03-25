{
  pkgs,
  lib,
  config,
  inputs,
  ...
}:

{

  # https://devenv.sh/basics/
  env.GREET = "devenv";

  # https://devenv.sh/packages/
  # packages = [ pkgs.git ];

  languages.java = {
    enable = true;
    jdk.package = pkgs.jdk23;
    maven.enable = true;
  };

  languages.javascript = {
    enable = true;
    npm.enable = true;
    package = pkgs.nodejs_23;
  };

  services.mysql = {
    enable = true;
    package = pkgs.mysql80;
    ensureUsers = [
      {
        name = "root";
        password = "123";
      }
    ];
    initialDatabases = [
      {
        name = "quiz";
        schema = ./db/quiz.sql;
      }
    ];
  };

  services.redis.enable = true;
  
  # https://devenv.sh/languages/
  # languages.rust.enable = true;

  # https://devenv.sh/processes/
  # processes.cargo-watch.exec = "cargo-watch";

  # https://devenv.sh/services/
  # services.postgres.enable = true;

  # https://devenv.sh/scripts/
  scripts.hello.exec = ''
    echo hello from $GREET
  '';

  enterShell = ''
    hello
    java --version
    node -v
    npm -v
  '';

  # https://devenv.sh/tasks/
  # tasks = {
  #   "myproj:setup".exec = "mytool build";
  #   "devenv:enterShell".after = [ "myproj:setup" ];
  # };

  # https://devenv.sh/tests/
  # enterTest = ''
  #   echo "Running tests"
  #   git --version | grep --color=auto "${pkgs.git.version}"
  # '';

  # https://devenv.sh/pre-commit-hooks/
  # pre-commit.hooks.shellcheck.enable = true;

  # See full reference at https://devenv.sh/reference/options/
}
